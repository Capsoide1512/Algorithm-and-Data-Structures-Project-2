package it.unicam.cs.asdl2324.mp2;

import java.util.Map;
import java.util.Set;
import java.util.*;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) NICOLA, CAPANCIONI
 *         nicola.capancioni@studenti.unicam.it
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;
        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;
        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;
        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }
    }
    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {           //costruttore
        currentElements = new HashMap<>();  //creo una HashMap che conterrà gli elementi e i nodi nell'albero
    }
    @Override
    public boolean isPresent(E e) {             //metodo che controlla se un elemento specifico è presente nella mappa
        return currentElements.containsKey(e);  //ritorna la chiave dell'elemento per controllare se è presente nella mappa
    }
    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {                  //creo un nuovo set con un solo elemento
        if(e == null){                          //controllo se l'elemento è nullo
            throw new NullPointerException("L'elemento è nullo!");
        }
        if(currentElements.containsKey(e)){     //controllo se l'elemento è già presente nella mappa
            throw new IllegalArgumentException("L'elemento passato è presente!");
        }
        Node<E> a = new Node<>(e);              //creo un nuovo oggetto di tipo nodo e lo assegno ad a
        currentElements.put(e,a);               //assegno l'elemento "e" come chiave e il nodo "a" associato a quell'elemento come valore nella HashMap
    }
    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {                     //ricerca del rappresentante
        if(e == null){                          //controllo che l'elemento non sia null
            throw new NullPointerException("L'elemento è nullo!");
        }

        Node<E> node =currentElements.get(e);   //ricavo il nodo associato all'elemento dell'hashmao

        if(node == null){                       //se il nodo non viene trovato
            return null;                        //ritorno null
        }else if(node == node.parent){          //altrimenti se il nodo è il root del set
            return node.item;                   //ritorno l'elemento presente del nodo
        }
        E root = findSet(node.parent.item);     //mi ricavo la radice del set per ottenere il nodo associato
        Node<E> a = currentElements.get(root);  //conterrà il nodo corrispondente alla radice del set a cui appartiene l'elemento rappresentato da root
        node.parent = a;                        //collego il nodo attuale alla radice
        return root;                            //ritorno la radice del set
    }
    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {             //unisco due set che sono gli elementi e1 ed e2
        if((e1 == null) || (e2 == null)){       //controllo se uno dei due elementi o entrambi sono null
            throw new NullPointerException("Uno dei due elementi o entrambi è nullo!");
        }                                       //controllo se uno dei due elementi o entrambi non sono presenti
        if((!currentElements.containsKey(e1)) || (!currentElements.containsKey(e2))){
            throw new IllegalArgumentException("Uno dei due elementi passati o enrambi non è presente.");
        }
        E a = findSet(e1);                      //trovo il rappresentante del set 1
        E b = findSet(e2);                      //trovo il rappresentante del set 2
        if(a == b){                             //controllo se i rappresentanti sono gli stessi ritorno null perchè i due elementi sono
            return;                             //gia nello stesso set
        }
        Node<E> node1 = currentElements.get(a);//ottengo i nodi associati al rappresentante a
        Node<E> node2 = currentElements.get(b);//ottengo i nodi associati al rappresentante b
        uniscoSetRank(node1, node2);           //richiamo il metodo per unire i due set
    }
    private void uniscoSetRank(Node<E> e1,Node<E> e2){      //unnisco due nodi in base al rank
        if(e1.rank > e2.rank){                              //se il rank di e1 è > di e2
            e2.parent = e1;                                 //e2 diventa il figlio di e1
        }else{
            e1.parent = e2;                                 //senno e1 diventa figlio di e2
            if(e1.rank == e2.rank){                         //se hanno lo stesso rank
                e2.rank++;                                  //incremento il rank di e2 per mantenere l'altezza degli alberi
            }
        }
    }
    @Override
    public Set<E> getCurrentRepresentatives() {                 //ritorno un insieme che ha tutti i rappresentanti di tutti gli insiemi presenti
        Set<E> set = new HashSet<>();                           //creo un nuovo insieme che conterrà i rappresentanti
        Set<E> currentElementsKeySet = currentElements.keySet();//prendo l'insieme delle chiavi dell'hashmap
        Iterator<E> iteratore = currentElementsKeySet.iterator();//creo un iteratore per scorrere le chiavi

        for (int i = 0; i < currentElementsKeySet.size(); i++) {//scorro le chiavi
            E currentElement = iteratore.next();                 //prendo l'elemento dall'iteratore
            E rappresentante = findSet(currentElement);         //prendo il rappresentante a cui appartiene l'elemento
            set.add(rappresentante);                            //aggiungo il rappresentante
        }
        return set;                                             //ritorno il set contenente tutti i rappresentanti
    }
    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if(e == null){                                  //controllo se l'elemento è nullo
            throw new NullPointerException("L'elemento passato è nullo!");
        }
        if(!currentElements.containsKey(e)){            //controllo se l'elemento è presente
            throw new IllegalArgumentException("L'elemento passato non è presente in nessun insieme!");
        }
        Set<E> elements = new HashSet<>();                      //creo un un nuovo set (insieme) per contenere gli elementi
        E radice = findSet(e);                                  //trova la radice rappresentante dell'elemento passato
        Set<E> currentElementsKeySet = currentElements.keySet();//prendo l'insieme delle chiavi dell'hashmap
        Iterator<E> iteratore = currentElementsKeySet.iterator();//creo un iteratore per scorrere tutte le chiavi

        for (int i = 0; i < currentElementsKeySet.size(); i++) {//scorro tutte le chiavi
            E currentElement = iteratore.next();                 //ottengo l'elemento
            if (findSet(currentElement) == radice) {            //se l'elemento appartiene allo stesso insieme (ha la stessa radice)
                elements.add(currentElement);                   //aggiungo l'elemento al set currentElement.
            }
        }
        return elements;                                        //ritorno l'insieme che ha l'elemento
    }
    @Override
    public void clear() {                   //rimuovo tutti gli elementi e le associazioni
        currentElements = new HashMap<>();  //creo una nuova mappa vuota che mi servirà per rimuovere tutti gli elementi e le associazioni negli insiemi
    }
}
