package it.unicam.cs.asdl2324.mp2;

import java.util.Comparator;
import java.util.Set;
import java.util.*;


/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) NICOLA, CAPANCIONI
 *         nicola.capancioni@studenti.unicam.it
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    private class Comparatore implements Comparator<GraphEdge<L>> { //comparatore che mi serve per cnfrontare due archi del grafo attraverso il loro peso

        @Override
        public int compare(GraphEdge<L> o1, GraphEdge<L> o2) {      //creo un metodo per comparare gli archi
            double peso1 = o1.getWeight();                          //prendo il peso del primo arco da confrontare e lo metto in una var d'appoggio
            double peso2 = o2.getWeight();                          //prendo il peso del secondo arco da confrontare e lo metto in una var d'appoggio
            if (peso1 < peso2) {                                    //se il primmo è minore del secondo
                return -1;                                          //significa che o1 precede o2
            } else if (peso1 > peso2) {                             //senno se il secondo è maggiore del secondo
                return 1;                                           //significa che o2 precede o1
            } else {
                return 0;                                           //sennò i pesi sono uguali
            }
        }
    }
    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     * copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if(g == null) {                                                                     //controllo che il grafo non sia null
            throw new NullPointerException("Il grafo è nullo!");                            //eccezzionale veramente
        }
        if(g.isDirected()) {                                                                //controllo se il grafo è orientato
            throw new IllegalArgumentException("Il grafo è orientato!");                    //eccezzionale veramente
        }
        this.disjointSets.clear();                                                          //pulisco il set per creare gli insiemi disgiunti separati per tutti i nodi del grafo
        Set<GraphEdge<L>> edges = new HashSet<>();                                          //definisco un nuovo insieme che conterrà gli archi dell'albero con la somma dei pesi minori

        Iterator<GraphNode<L>> Iteratore = g.getNodes().iterator();                      //utilizzo un iteratore per scorrere i nodi del grafo
        while(Iteratore.hasNext()) {                                                     //itera tutti i nodi fino a quando non ci sono nodi successivi da esaminare
            GraphNode<L> node = Iteratore.next();                                        //prende il successivo
            disjointSets.makeSet(node);                                                     //per ogni nodo richiamo il metodo makeSet
        }


        ArrayList<GraphEdge<L>> edgesList = new ArrayList<>(g.getEdges());                  //creo un iteratore per scorrere la lista degli archi
        sorted(edgesList);                                                                  //richiamo il metodo che mi ordinerà la lista (tramite bubblesort)
        Iterator<GraphEdge<L>> Iteratore2 = edgesList.iterator();                         //metto nell'iteratore la lista di archi

        while(Iteratore2.hasNext()) {                                                            //utilizzo iterator per iterare la lista degli archi
            GraphEdge<L> edge = Iteratore2.next();                                                //prendo il prossimo elemento
            if (!edge.hasWeight() || edge.getWeight() < 0) {                                        //controllo ogni arco se ha pesi negati o non è pesato
                throw new IllegalArgumentException("Non è pesato o contiene pesi negativi.");       //lancio eccezzione nel caso lo siano
            }
            if (disjointSets.findSet(edge.getNode1()) != disjointSets.findSet(edge.getNode2())) {   //controllo che il findSet è diverso dai due nodi dell'arco
                edges.add(edge);                                                                    //aggiunge l'arco all'HashSet
                disjointSets.union(edge.getNode1(), edge.getNode2());                               //richiamo il metodo union per unire i nodi
            }
        }
        return edges;
    }
    private void sorted(List<GraphEdge<L>> edgesList) {                                 //faccio un bubblesort per ordinare la lista degli archi
        boolean scambio;                                                                //variabile  boolean per utilizzata per verificare se è stato effettuato uno scambio
        int n = edgesList.size();                                                       //numero di archi nella lista
        Comparatore comparatore = new Comparatore();                                    //creo un'istanza di Comparatore per fare il confronto tra gli archi

        do{
            scambio = false;                                                            //pongo lo scambio a falso (non sono stati effettuati scambi)
            for(int i = 1; i < n; i++) {                                               //scorro tutti gli arch della lista
                if (comparatore.compare(edgesList.get(i - 1), edgesList.get(i)) > 0) { //se gli elementi sono nell'ordine sbagliato li scambio
                    GraphEdge<L> app = edgesList.get(i - 1);                            //creo una variabile di appoggio che mi servirà per effettuare lo scambio
                    edgesList.set(i - 1, edgesList.get(i));                             //effettuo lo scambio
                    edgesList.set(i, app);                                              //sostituisco l'elemento in "i" con l'elemento in "app" per ordinare
                    scambio = true;                                                     //pongo a true per dire che lo scambio è stato effettuato e fermare l'iterazione
                }
            }
        } while (scambio);
    }
}
