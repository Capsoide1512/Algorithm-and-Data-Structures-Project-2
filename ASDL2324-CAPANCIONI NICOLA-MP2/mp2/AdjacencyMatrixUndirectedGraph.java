/**
 *
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.*;


// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 *
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template) NICOLA, CAPANCIONI
 *         nicola.capancioni@studenti.unicam.it
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */
    Set<GraphEdge<L>> arches = new HashSet<>();     //creo un oggetto di tipo HashSet, il set conterrà un insieme di elementi non duplicati e ordinati(arches)
                                                    //arches è un oggetto che rappresenta l'arco del grafo
    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;    //mappa che tiene traccia degli indici associati ai nodi
    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;    //matrice di archi del grafo, la presenza di un oggetto "GraphEdge<L>" indica la presenza di un arco tra i nodi nella matrice.
                                                            //è stato utilizzato un array list al fine di avere un oggetto ridimensionabile per effettuare l'accesso agli elementi
                                                            //in modo dinamico
    /**Crea un grafo vuoto*/

    //inizializzo il costruttore del grafo non-oriented con matrice di adiacenza
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();     //matrice degli archi
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();     //indice dei nodi
    }
    @Override
    public int nodeCount() {
        return this.nodesIndex.size();          //ritorno il numero di nodi del grafo
    }
    @Override
    public int edgeCount() {        //conta gli archi senza i duplicati di un grafo non orientato
        int c = 0;                  //contatore
        //scorro le righe della matrice
        for (int i = 0; i < this.matrix.size(); i++) {
            ArrayList<GraphEdge<L>> row = this.matrix.get(i);       //ottengo la riga corrente della matrice che conterrà gli archi

            //scorro gli elementi della riga sulla matrice
            for (int j = 0; j < row.size(); j++) {
                GraphEdge<L> edge = row.get(j);                     //ottengo la lista degli archi

                if ((edge != null) && (j <= i)) {                   //controllo che gli archi non siano nulli e non siano duplicati
                    c++;
                }
            }
        }
        return c;                                                           //ritorno il numero di archi (non duplicati) nel grafo
    }
    @Override
    public void clear() {           //rimuove tutti i nodi e gli archi dal grafo (il grafo diventa vuoto)
        this.matrix.clear();        //pulisce tutte le righe della matrice
        this.nodesIndex.clear();    //pulisce tutti i nodi dall'indice dei nodi
    }
    @Override
    public boolean isDirected() {       //controlla se il grafo è orientato
        return false;                   //ritorno false perchè il grafo non è orientato
    }
    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {                     //aggiunta di un nuovo nodo al grafo
        if (node == null || node.getLabel() == null) {              //controllo se il nodo o l' etichetta sono null
            throw new NullPointerException("Il nodo o il valore dell'etichetta del nodo non può essere nullo");
        }
        if (this.nodesIndex.containsKey(node)) {                    //controllo se il nodo è già presente
            return false;                                           //il nodo è già presente, non aggiunge niente
        }
        int newIndiceNodo = this.nodeCount();                       //variabile d'appoggio contenente l'indice de nuovo nodo nel grafo
        this.nodesIndex.put(node, newIndiceNodo);                   //inserisce il nodo all'indice corrispondente

        //aggiunge una nuova colonna alla matrice degli archi
        for (int i = 0; i< matrix.size(); i++ ){
            ArrayList<GraphEdge<L>> graphEdges = matrix.get(i);     //ottiene la lista di archi associata al nodo nella posizione i
            graphEdges.add(null);                                   //aggiunta di un elemento nullo alla lista degli archi nell matrice
        }                                                           //l'elemento viene impostato (inizialmento) come nullo per indicare l'assenza
                                                                    //di archi associati al nuovo nodo aggiunto
        //aggiunge una nuova riga alla matrice
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>();
        for(int i = 0; i< nodesIndex.size(); i++){
            newRow.add(null);                                       //viene aggiunto un elemento nullo a newRow per inizializzare la nuova riga della
        }                                                           //matrice rappresentante gli archi associati al nuovo nodo aggiunto
        this.matrix.add(newRow);                                    //aggiungo una nuova riga alla matrice rapp
        return true;                                                //il nodo è stato aggiunto al grafo correttamente
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override //label = etichetta che identifica univocamente un nodo ad un grafo
    public boolean addNode(L label) {               //aggiunta di un nuovo nodo con l'etichetta specificata al grago
        if (label == null) {                        //controllo che l'etichetta del nuovo nodo non sia null
            throw new NullPointerException("Il valore dell'etichetta del nodo non può essere nullo!");
        }
        GraphNode<L> newNode = new GraphNode<>(label);  //creao un nuovo nodo con l'etichetta specificata
        return this.addNode(newNode);                   //richiamo il metodo di addNode(GraphNode<L> node) per l'aggiunta del nodo al grafo
                                                        //e lo ritorna
    }
    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {         //rimozione di un nodo dal grafo
        if (node == null) {                             //controllo che il nodo non sia nullo
            throw new NullPointerException("Il nodo passato non può essere nullo!");
        }                                               //controllo che il nodo sia presente nel grafo
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Non esiste il nodo in questo grafo.");
        }
        int removedIndex = nodesIndex.remove(node); //variabile d'appoggio contenente l'indice del nodo da rimuovere

        for (Map.Entry<GraphNode<L>, Integer> a : nodesIndex.entrySet()) {      //scorro le chiavi
            //controllo che il valore sia maggiore dell'indice rimosso
            //peridurre la dimensione della matrice
            if (a.getValue() > removedIndex) {
                int nuovoIndice = a.getValue() - 1;                      //decreneto l'indice del nodo dopo la rimozione
                nodesIndex.put(a.getKey(), nuovoIndice);                 //metto al nodo il nuovo indice del nuovo valore
            }
        }
        matrix.remove(removedIndex);                                    //rimuovo la riga dell'indice rimosso (riadattamento matrice)

        for (int i = 0; i < matrix.size(); i++) {                       //scorro la matrice per eliminare la colonna dell'indice rimosso (riadattamento matrice)
            matrix.get(i).remove(removedIndex);                         //elimino la colonna
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        GraphNode<L> node = this.getNode(label);    //prendo il nodo
        if(node!=null){                             //controllo che il nodo non sia null (esiste)
            removeNode(node);                       //rimuovo il nodo
        }else{
            throw new IllegalArgumentException();   //se nullo lancio l'eccezzione
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {                     //rimozione nodo dal grafo
        GraphNode<L> node = this.getNode(i);            //dato l'indice ottengo il nodo dal grafo
        this.removeNode(node);                          //richiamo il metodo per rimuovere il nodo
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {    //restituisco un nodo dal grafo dato un nodo specifico
        if (node == null) {                             //controllo se il nodo è null, se si lancio eccezione
            throw new NullPointerException("Il nodo passato non può essere nullo!");
        }
        if (this.nodesIndex.containsKey(node)) {        //se il grafo contiene il nodo
            return node;                                //restituisco il nodo
        } else return null;                             //altrimenti restituisco null
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null) {                                                     //controllo se l'etichetta passata è null, se si lancio eccezione
            throw new NullPointerException("L'etichetta non può essere nulla!");
        }
            Set<GraphNode<L>> keySet = nodesIndex.keySet();                     //ottengo l'insieme delle chiavi dalla mappa
            Iterator<GraphNode<L>> iteratore = keySet.iterator();               //creo un iteratore che itererà sull'insieme delle chiavi

            while (iteratore.hasNext()) {                                       //itero l'insieme delle chiavi
                GraphNode<L> node = iteratore.next();                           //prendo il prossimo nodo
                if (node.getLabel().equals(label)) {                            //se il nodo ha la stessa etichetta di quella passata
                    return node;                                                //se ha la stessa etichetta restituisco il nodo
                }
            }
            return null;                                                        //se invece non hanno la stessa etichetta restituiso null
    }

    @Override
    public GraphNode<L> getNode (int i) {
        if ((i < 0) || (i >= this.nodeCount())) {                       //controllo se l'indice che ho passato rispetta i limiti
            throw new IndexOutOfBoundsException("L'indice passato non valido o al di fuori dei limiti consentiti!");
        }
        Iterator<Map.Entry<GraphNode<L>, Integer>>                      //creo un iteratore che scorrerà gli elementi della mappa di nodesIndex
                iteratore = nodesIndex.entrySet().iterator();
        while(iteratore.hasNext()) {                                    //itero gli elementi
            Map.Entry<GraphNode<L>, Integer> entry = iteratore.next();  //prendo il chiave e il valore dalla map
            if (entry.getValue().equals(i)) {                           //se il valore associato alla key è uguale all'indice
                return entry.getKey();                                  //ritorno la key del nodo
            }
        }
        return null;                                                    //senno restituisco null se nessun nodo con l'indice desiderato è stato troato
    }

    @Override
    public int getNodeIndexOf (GraphNode<L> node) {
        if (node == null){                      //controllo se il nodo è nullo, se si lancio eccezione
            throw new NullPointerException("Il nodo passato non può essere nullo!");
        }
        if(!nodesIndex.containsKey(node)){      //controllo se il nodo è presente nel grafo, se si lancio eccezione
            throw new IllegalArgumentException("Il nodo passato non è presente in questo grafo!");
        }
        return this.nodesIndex.get(node);       //ritorno il valore che è associato alla chiave del nodo nell mappa
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) {                        //se l'etichetta è nulla lancio eccezione
            throw new NullPointerException("L'etichetta passata non può essere nulla!");
        }
        GraphNode<L> node = this.getNode(label);    //prendo il nodo dal grafo grazie all'etichetta
        if (node == null) {                         //se il nodo preso è nullo lancio eccezione
            throw new IllegalArgumentException("Il nodo è nullo!");
        }
        return this.getNodeIndexOf(node);           //ritorno l'indice del nodo ottenuto
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();            //ritorno l'insieme di tutti i nodi presenti nel grafo
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {     //se l'arco è nullo lancio eccezione
        if (edge == null){
            throw new NullPointerException("L'arco passato non può essere nullo!");
        }
        if(edge.isDirected()){                      //se l'arco è gia presente nel grafo lancio eccezione
            throw new IllegalArgumentException("L'arco è già presente nel grafo!");
        }

        GraphNode<L> node1 = this.getNode(edge.getNode1());     //ottengo i nodi associati all'arco
        GraphNode<L> node2 = this.getNode(edge.getNode2());

        if (node1 == null || node2 == null) {                   //controllo se i nodi esistono nel grafo, se non esistono lancio eccezione
            throw new IllegalArgumentException("Uno dei due nodi o entrambi non esiste!");
        }

        int i = this.getNodeIndexOf(node1);         //variabile interna nodo1
        int j = this.getNodeIndexOf(node2);         //variabile interna nodo2

        if ((this.matrix.get(i).get(j) != null) ||
                (this.matrix.get(j).get(i) != null) &&
                (i==-1)||(j==-1)) {
            return false;                           //l'arco e' già presente e gli indici non sono validi
        }

        this.matrix.get(j).set(i, edge);            //setto l'arco alla matrice degli archi
        this.matrix.get(i).set(j, edge);
        arches.add(edge);                           //add dell'arco al set di archi
        return true;                                //ritorno treu se l'arco è stato aggiunto correttamente
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) {                               //controllo se i nodi esistono, se non esistono lancio eccezione
            throw new NullPointerException("Uno dei due nodi o entrambi non esiste!");
        }
        GraphEdge<L> newEdge = new GraphEdge<>(node1, node2, false);//creo un nuovo arco attraverso i nodi forniti
        return this.addEdge(newEdge);                                       //richiamo addEdge per ritornare l'arco al grafo (e aggiungerlo)
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        if (node1 == null || node2 == null) {
            throw new IllegalArgumentException("Uno dei due nodi o entrambi non esiste!"); // Nodi non validi
        }
        GraphEdge<L> newEdge = new GraphEdge<>(node1, node2, false, weight);    //creo un arco con i pesi, utilizzando i nodi che già avevo
        return this.addEdge(newEdge);                                                   //richiamo addEdge per ritornare e aggiungere l'arco al grafo
    }
    @Override
    public boolean addEdge(L label1, L label2) {
        GraphNode<L> node1 = this.getNode(label1);      //ottengo i nodi del grafo attraverso le etichette
        GraphNode<L> node2 = this.getNode(label2);
        if (node1 == null || node2 == null) {           //se uno dei due nodi è nullo lancio eccezione
            throw new IllegalArgumentException(("Una delle due etichette o entrambe è nulla!")); // Nodi non validi
        }
        return this.addEdge(node1, node2);          //richiamo addedge per restitutire l'arco al grafo
    }
    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        GraphNode<L> node1 = this.getNode(label1);
        GraphNode<L> node2 = this.getNode(label2);
        if (node1 == null || node2 == null) {
            throw new IllegalArgumentException("Una delle due etichette o entrambe è nulla!"); // Nodi non validi
        }
        return this.addWeightedEdge(node1, node2, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        if (node1 == null || node2 == null) {
            return false;
        }
        return this.addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        return this.addWeightedEdge(node1, node2, weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null) {                     //controllo se l'arco è nullo, se si lancio eccezione
            throw new NullPointerException("L'arco passato è nullo!");
        }
        //mi ricavo i nodi associati all'arco
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();

        if (node1 == null || node2 == null) {   //se i nodi non esistono lancio eccezione
            throw new IllegalArgumentException("Uno dei due nodi è mancante");
        }
        //mi ricavo gli indici dei nodi nel grafo
        int i = nodesIndex.get(node1);
        int j = nodesIndex.get(node2);
        //elimino l'arco settando null sulle posizioni nella matrice degli archi
        matrix.get(i).set(j, null);
        matrix.get(j).set(i, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if ((node1 == null) || (node2 == null)) {                   //controllo se i nodi esistono senno lancio eccezione
            throw new NullPointerException("Uno dei due nodi è mancante");
        }
        GraphEdge<L> edgeToRemove = getEdge(node1, node2);          //mi ricavo l'arco associato ai nodi
        if (edgeToRemove == null) {                                 //controllo che l'arco da rimuovere non sia nullo
            throw new IllegalArgumentException("Arco mancante!");
        }
        removeEdge(edgeToRemove);                                   //rimuovo l'arco richiamando removeEdge
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if ((label1 == null) || (label2 == null)) {             //controllo se le etichette esistono senno lancio eccezione
            throw new NullPointerException("Uno dei due label è nullo!");
        }
        GraphEdge<L> edgeToRemove = getEdge(label1, label2);    //mi ricavo l'arco associato alle etichette specificate
        if (edgeToRemove == null) {                             //controllo se l'arco da eliminare esiste, senno lancio eccezione
            throw new IllegalArgumentException("Arco mancante!");
        }
        removeEdge(edgeToRemove);                               //rimuovo l'arco richiamando il metodo removeEdge
    }

    @Override
    public void removeEdge(int i, int j) {
        if ((i < 0                              //controllo se gli idici sono corretti nei limiti della matrice, senno lancio un eccezione
                || i > matrix.size() - 1
                || j < 0
                || j > matrix.size() - 1)) {
            throw new IndexOutOfBoundsException("Indice non valido!");
        }
        //rimuovo l'arco settando null sulle posizioni nella matrice degli archi
        matrix.get(i).set(j, null);
        matrix.get(j).set(i, null);
    }


    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null) {                     //controllo se l'arco esiste
            throw new NullPointerException("L'arco passato è nullo!");
        }
        GraphNode<L> node1 = edge.getNode1();       //ottengo nodo 1 associato all'arco
        GraphNode<L> node2 = edge.getNode2();       //ottengo nodo 2 associato all'arco
        if ((!nodesIndex.containsKey(node1) || (!nodesIndex.containsKey(node2)))) { //se uno dei due nodi associati non esiste lancio eccezione
            throw new IllegalArgumentException("L'arco non esiste!");
        }
        return matrix.get(nodesIndex.get(node1)).get(nodesIndex.get(node2));        //ritorno l'arco  della matrice attraverso gli indici associati ai nodes
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if ((node1 == null) || (node2 == null)) {                                   //controllo che i nodi non siano nulli, se si lancio un'eccezione
            throw new NullPointerException("Uno dei due nodi o entrambi non può essere nullo!");
        }
        if ((!nodesIndex.containsKey(node1) || (!nodesIndex.containsKey(node2)))) { //controllo se i nodi appartengono al grafo, sennò lancio eccezione
            throw new IllegalArgumentException("Il nodo non appartiene al grafo.");
        }
        return getEdge(new GraphEdge<>(node1, node2, false));               //ritorno l'arco associato ai nodi richiamando getEdge
    }
    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if ((label1 == null || (label2 == null))) {                                 //controllo se le etichette sono null, se si lancio eccezione
            throw new NullPointerException("Una delle due etichette o entrambe è null!");
        }
        return getEdge(new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), false));   //ritorno l'arco associato alle etichette richiamando getEdge
    }
    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if ((i < 0 || i >= matrix.size()) || (j < 0 || j >= matrix.size())) {   //controllose gli indici sono fuori dai limiti della matrice
            throw new IndexOutOfBoundsException("L'indice non è valido!");
        }
        if (getNode(i) == null || getNode(j) == null)                           //controllo se uno dei nodi esiste nel grafo
            throw new IndexOutOfBoundsException("Uno dei nodi non è presente nella mappa");
        return matrix.get(i).get(j);                                            //ritorno l'arco della matrice attraverso i suoi indici
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if(node==null) {                                    //controllo se il nodo è nullo o non presente
            throw new NullPointerException("Nodo nullo!");  //se è nullo lancio un eccezione
        }else if(getNode(node)==null) {
            throw new IllegalArgumentException("Node non presente!");
        }

        Set<GraphNode<L>> nuovoSet=new HashSet<>();         //creo un nuovo set per memorizzare i nodi adiacenti
        Iterator<GraphNode<L>> iteratore = getNodes().iterator();//creo un iteratore per scorrere i nodi del grafo

        while(iteratore.hasNext()) {                        //itero i nodi
            GraphNode<L> mapNode = iteratore.next();
            if(getEdge(node, mapNode) != null) {            //controllo se esiste un arco tra il nodo preso e quello corrente
                nuovoSet.add(mapNode);                      //se l'arco esiste gli aggiungo il nodo corrente al set degli adiacenti
            }
        }
        return nuovoSet;                                    //ritorno il set dei nodi adiacenti
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label==null) {                  //se l'etichetta è nulla lancio eccezione
            throw new NullPointerException("Etichetta nulla!");
        }
        return getAdjacentNodesOf(new GraphNode<>(label));  //ritorno il nodo creato attraverso l'etichetta richiamando getAdjecentOf
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if((i < 0) || (i > this.matrix.size() - 1)) {   //controllo se l'indice è fuori dai limiti della matrice, se si lancio eccezione
            throw new IndexOutOfBoundsException("Indice non valido!");
        }
        return getAdjacentNodesOf(this.getNode(i));     //ritorno il nodo ottenuto dall'indice fornito richiamando getAdjacentNodesOf
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if(node==null) {                //controllo se il nodo è nullo, se si lancio eccezione
            throw new NullPointerException("Node nullo!");
        }else if(getNode(node)==null) { //controllo se il nodo è presente nel grafo, se non presente lancio eccezione
            throw new IllegalArgumentException("Nodo non presente!");
        }
        Set<GraphEdge<L>> nuovoSet=new HashSet<>();                                 //creo un  set per memorizzare gli archi del nodo
        ArrayList<GraphEdge<L>> edgeList = this.matrix.get(nodesIndex.get(node));   //mi ricavo l'elenco degli archi associati al nodo dalla matrice degli archi

        for (int i = 0; i < edgeList.size(); i++) {                                  //scorro gli  archi nella lista
            GraphEdge<L> element = edgeList.get(i);
            if (element != null) {                                                  //se non sono nulli
                nuovoSet.add(element);                                              //li aggiungo al set
            }
        }
        return nuovoSet;                                                            //ritorno il set
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if(label==null) {                                   //controllo che l'etichetta non sia null, se si lancio eccezione
            throw new NullPointerException("Lable nullo!");
        }
        return getEdgesOf(new GraphNode<>(label));          //ritorno il nodo creato richiamando getEdges
    }
    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (((i<0)||(i>this.matrix.size()-1))) {                        //controllo se l'indice è fuori dai limiti della matrice
            throw new IndexOutOfBoundsException("Indice non valido!");  //se si lancio eccezione
        } else if(getNode(i)==null) {                                   //controllo se il nodo associato all'indice esiste nel grafo
            throw new IndexOutOfBoundsException("Nodo non preente!");
        }
        Set<GraphEdge<L>> newSet=new HashSet<>();                       //creo un un set per inserire gli archi del nodo associato all'indice
        Set<GraphNode<L>> adjacentNodes = getAdjacentNodesOf(i);        //mi ricavo i nodi adiacenti al nodo associato all'indice
        Iterator<GraphNode<L>> iteratore = adjacentNodes.iterator();    //creo un iteratore per scorrere i nodi adiacenti
        while (iteratore.hasNext()) {                                   //scorro i nodi adiacenti
            GraphNode<L> mapNode = iteratore.next();
            newSet.add(getEdge(getNode(i), mapNode));                   //aggiungo gli archi corrispondenti al set
        }
        return newSet;                                                  //ritorno il set degli archi associati al nodo
    }
    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }
    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }
    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }
    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> nuovoSet = new HashSet<>();           //creo un set per salvere tutti gli archi del grafo

        for (int i = 0; i < this.matrix.size(); i++) {          //scorro le righe della matrice degli archi
            ArrayList<GraphEdge<L>> list = this.matrix.get(i);  //mi ricavo la lista degli archi associati al nodo dato
            for (int j = 0; j < list.size(); j++) {             //scorro gli archi nella lista
                GraphEdge<L> edge = list.get(j);
                if (edge != null) {                             //se non sono null
                    nuovoSet.add(edge);                         //li aggiungi al set
                }
            }
        }
        return nuovoSet;                                        //ritorno il set di tutti gli archi nel grafo
    }
}