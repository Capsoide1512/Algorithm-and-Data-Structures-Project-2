package it.unicam.cs.asdl2324.mp2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Classe di test per la classe ForestDisjointSets.
 * 
 * @author Luca Tesei
 *
 */
class ForestDisjointSetsTest {

    @Test
    final void testForestDisjointSets() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
    }

    @Test
    final void testIsPresent() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertFalse(ds.isPresent(1));
        ds.makeSet(1);
        assertTrue(ds.isPresent(1));
        ds.makeSet(2);
        assertTrue(ds.isPresent(2));
        assertFalse(ds.isPresent(3));
    }

    @Test
    final void testIsPresentAfterUnion() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.union(1, 2);
        assertTrue(ds.isPresent(1));
        assertTrue(ds.isPresent(2));
        assertFalse(ds.isPresent(3));
        ds.makeSet(3);
        assertTrue(ds.isPresent(3));
        ds.union(2, 3);
        assertTrue(ds.isPresent(1));
        assertTrue(ds.isPresent(2));
        assertTrue(ds.isPresent(3));
    }

    @Test
    final void testMakeSetExceptions() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertThrows(NullPointerException.class, () -> ds.makeSet(null));
        ds.makeSet(1);
        assertThrows(NullPointerException.class, () -> ds.makeSet(null));
        ds.makeSet(2);
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(1));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(2));
        ds.makeSet(3);
        ds.union(1, 3);
        assertThrows(NullPointerException.class, () -> ds.makeSet(null));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(1));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(2));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(3));
        ds.union(1, 2);
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(1));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(2));
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(3));
    }

    @Test
    final void testMakeSet() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ForestDisjointSets.Node<Integer> n1 = ds.currentElements.get(1);
        assertTrue(n1.item.equals(1));
        assertTrue(n1.parent == n1);
        assertTrue(n1.rank == 0);
        assertTrue(ds.findSet(1).equals(1));
        ds.makeSet(2);
        ForestDisjointSets.Node<Integer> n2 = ds.currentElements.get(2);
        assertTrue(n2.item.equals(2));
        assertTrue(n2.parent == n2);
        assertTrue(n2.rank == 0);
        assertTrue(ds.findSet(2).equals(2));
    }

    @Test
    final void testFindSetExceptions() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertThrows(NullPointerException.class, () -> ds.findSet(null));
        ds.makeSet(1);
        assertThrows(NullPointerException.class, () -> ds.findSet(null));
        assertTrue(ds.findSet(1).equals(1));
        assertTrue(ds.findSet(2) == null);
    }

    @Test
    final void testFindSet() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);
        assertTrue(ds.findSet(1).equals(1));
        assertTrue(ds.findSet(2).equals(2));
        assertTrue(ds.findSet(3).equals(3));
        assertTrue(ds.findSet(4).equals(4));
        assertTrue(ds.findSet(5).equals(5));
        assertTrue(ds.findSet(6) == null);
        ds.union(1, 2);
        assertTrue(ds.findSet(2) == ds.findSet(1));
        ds.union(3, 4);
        assertTrue(ds.findSet(3) == ds.findSet(4));
        ds.union(5, 3);
        assertTrue(ds.findSet(4) == ds.findSet(5));
        assertTrue(ds.findSet(3) == ds.findSet(4));
        ds.union(1, 5);
        assertTrue(ds.findSet(1) == ds.findSet(2));
        assertTrue(ds.findSet(2) == ds.findSet(3));
        assertTrue(ds.findSet(3) == ds.findSet(4));
        assertTrue(ds.findSet(4) == ds.findSet(5));
        assertTrue(ds.findSet(6) == null);
        ds.makeSet(6);
        assertTrue(ds.findSet(6).equals(6));
    }

    @Test
    final void testFindSetEuristicaCompressioneDelCammino() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);
        ds.union(1, 2);
        ForestDisjointSets.Node<Integer> n1 = ds.currentElements.get(1);
        ForestDisjointSets.Node<Integer> n2 = ds.currentElements.get(2);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n2);
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n2);
        ds.union(3, 4);
        ForestDisjointSets.Node<Integer> n3 = ds.currentElements.get(3);
        ForestDisjointSets.Node<Integer> n4 = ds.currentElements.get(4);
        assertTrue(n3.rank == 0);
        assertTrue(n3.parent == n4);
        assertTrue(n4.rank == 1);
        assertTrue(n4.parent == n4);
        ds.union(3, 5);
        ForestDisjointSets.Node<Integer> n5 = ds.currentElements.get(5);
        assertTrue(n5.rank == 0);
        assertTrue(n5.parent == n4);
        assertTrue(n4.rank == 1);
        assertTrue(n4.parent == n4);
        ds.union(1, 5);
        assertTrue(n4.rank == 2);
        assertTrue(n4.parent == n4);
        assertTrue(n3.rank == 0);
        assertTrue(n3.parent == n4);
        assertTrue(n5.rank == 0);
        assertTrue(n5.parent == n4);
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n4);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n2);
        assertTrue(ds.findSet(1).equals(4));
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n4);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n4);

        ds.makeSet(6);
        ds.makeSet(7);
        ds.union(6, 7);
        ForestDisjointSets.Node<Integer> n6 = ds.currentElements.get(6);
        ForestDisjointSets.Node<Integer> n7 = ds.currentElements.get(7);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 1);
        assertTrue(n7.parent == n7);

        ds.makeSet(8);
        ds.makeSet(9);
        ds.union(8, 9);
        ForestDisjointSets.Node<Integer> n8 = ds.currentElements.get(8);
        ForestDisjointSets.Node<Integer> n9 = ds.currentElements.get(9);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n9);

        ds.union(8, 6);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 2);
        assertTrue(n7.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n7);

        ds.makeSet(10);
        ds.makeSet(11);
        ds.union(11, 10);
        ForestDisjointSets.Node<Integer> n10 = ds.currentElements.get(10);
        ForestDisjointSets.Node<Integer> n11 = ds.currentElements.get(11);
        assertTrue(n10.rank == 1);
        assertTrue(n10.parent == n10);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);

        ds.makeSet(12);
        ds.makeSet(13);
        ds.union(12, 13);
        ForestDisjointSets.Node<Integer> n12 = ds.currentElements.get(12);
        ForestDisjointSets.Node<Integer> n13 = ds.currentElements.get(13);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n13);

        ds.union(12, 10);
        assertTrue(n10.rank == 2);
        assertTrue(n10.parent == n10);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n10);

        ds.union(10, 7);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 3);
        assertTrue(n7.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n7);
        assertTrue(n10.rank == 2);
        assertTrue(n10.parent == n7);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n10);

        assertTrue(ds.findSet(12).equals(7));
        assertTrue(n12.parent == n7);
        assertTrue(n12.rank == 0);
        assertTrue(n13.parent == n7);
        assertTrue(n13.rank == 1);

        assertTrue(ds.findSet(11).equals(7));
        assertTrue(n11.parent == n7);
        assertTrue(n11.rank == 0);

        assertTrue(ds.findSet(8).equals(7));
        assertTrue(n8.parent == n7);
        assertTrue(n8.rank == 0);

        ds.union(7, 2);

        assertTrue(n1.parent == n4);
        assertTrue(n1.rank == 0);
        assertTrue(n2.parent == n4);
        assertTrue(n2.rank == 1);
        assertTrue(n3.parent == n4);
        assertTrue(n3.rank == 0);
        assertTrue(n4.parent == n7);
        assertTrue(n4.rank == 2);
        assertTrue(n5.parent == n4);
        assertTrue(n5.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n6.rank == 0);
        assertTrue(n7.parent == n7);
        assertTrue(n7.rank == 3);
        assertTrue(n8.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n9.parent == n7);
        assertTrue(n9.rank == 1);
        assertTrue(n10.parent == n7);
        assertTrue(n10.rank == 2);
        assertTrue(n11.parent == n7);
        assertTrue(n11.rank == 0);
        assertTrue(n12.parent == n7);
        assertTrue(n12.rank == 0);
        assertTrue(n13.parent == n7);
        assertTrue(n13.rank == 1);

        assertTrue(ds.findSet(1).equals(7));
        assertTrue(n1.parent == n7);
        assertTrue(n1.rank == 0);
        assertTrue(n2.parent == n4);
        assertTrue(n2.rank == 1);
        assertTrue(ds.findSet(2).equals(7));
        assertTrue(n2.parent == n7);
        assertTrue(n2.rank == 1);

    }

    @Test
    final void testUnionEuristicaUnionePerRango() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);

        ds.union(1, 2);
        ForestDisjointSets.Node<Integer> n1 = ds.currentElements.get(1);
        ForestDisjointSets.Node<Integer> n2 = ds.currentElements.get(2);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n2);
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n2);

        ds.union(3, 4);
        ForestDisjointSets.Node<Integer> n3 = ds.currentElements.get(3);
        ForestDisjointSets.Node<Integer> n4 = ds.currentElements.get(4);
        assertTrue(n3.rank == 0);
        assertTrue(n3.parent == n4);
        assertTrue(n4.rank == 1);
        assertTrue(n4.parent == n4);

        ds.union(3, 5);
        ForestDisjointSets.Node<Integer> n5 = ds.currentElements.get(5);
        assertTrue(n5.rank == 0);
        assertTrue(n5.parent == n4);
        assertTrue(n4.rank == 1);
        assertTrue(n4.parent == n4);

        ds.union(1, 5);
        assertTrue(n4.rank == 2);
        assertTrue(n4.parent == n4);
        assertTrue(n3.rank == 0);
        assertTrue(n3.parent == n4);
        assertTrue(n5.rank == 0);
        assertTrue(n5.parent == n4);
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n4);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n2);

        ds.makeSet(6);
        ds.makeSet(7);

        ds.union(6, 7);
        ForestDisjointSets.Node<Integer> n6 = ds.currentElements.get(6);
        ForestDisjointSets.Node<Integer> n7 = ds.currentElements.get(7);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 1);
        assertTrue(n7.parent == n7);

        ds.makeSet(8);
        ds.makeSet(9);

        ds.union(8, 9);
        ForestDisjointSets.Node<Integer> n8 = ds.currentElements.get(8);
        ForestDisjointSets.Node<Integer> n9 = ds.currentElements.get(9);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n9);

        ds.union(8, 6);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 2);
        assertTrue(n7.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n7);

        ds.makeSet(10);
        ds.makeSet(11);

        ds.union(11, 10);
        ForestDisjointSets.Node<Integer> n10 = ds.currentElements.get(10);
        ForestDisjointSets.Node<Integer> n11 = ds.currentElements.get(11);
        assertTrue(n10.rank == 1);
        assertTrue(n10.parent == n10);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);

        ds.makeSet(12);
        ds.makeSet(13);

        ds.union(12, 13);
        ForestDisjointSets.Node<Integer> n12 = ds.currentElements.get(12);
        ForestDisjointSets.Node<Integer> n13 = ds.currentElements.get(13);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n13);

        ds.union(12, 10);
        assertTrue(n10.rank == 2);
        assertTrue(n10.parent == n10);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n10);

        ds.union(13, 6);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 3);
        assertTrue(n7.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n7);
        assertTrue(n10.rank == 2);
        assertTrue(n10.parent == n7);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n10);

        ds.union(7, 4);

        assertTrue(n4.rank == 2);
        assertTrue(n4.parent == n7);
        assertTrue(n3.rank == 0);
        assertTrue(n3.parent == n4);
        assertTrue(n5.rank == 0);
        assertTrue(n5.parent == n4);
        assertTrue(n2.rank == 1);
        assertTrue(n2.parent == n4);
        assertTrue(n1.rank == 0);
        assertTrue(n1.parent == n2);
        assertTrue(n6.rank == 0);
        assertTrue(n6.parent == n7);
        assertTrue(n7.rank == 3);
        assertTrue(n7.parent == n7);
        assertTrue(n8.rank == 0);
        assertTrue(n8.parent == n9);
        assertTrue(n9.rank == 1);
        assertTrue(n9.parent == n7);
        assertTrue(n10.rank == 2);
        assertTrue(n10.parent == n7);
        assertTrue(n11.rank == 0);
        assertTrue(n11.parent == n10);
        assertTrue(n12.rank == 0);
        assertTrue(n12.parent == n13);
        assertTrue(n13.rank == 1);
        assertTrue(n13.parent == n10);

    }

    @Test
    final void testUnion() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);

        ds.union(1, 2);
        assertTrue(ds.findSet(1).equals(ds.findSet(2)));
        assertTrue(ds.findSet(2).equals(ds.findSet(1)));
        assertFalse(ds.findSet(1).equals(ds.findSet(3)));
        assertFalse(ds.findSet(2).equals(ds.findSet(3)));
        assertTrue(ds.getCurrentRepresentatives().size() == 4);

        ds.union(2, 1);
        assertTrue(ds.findSet(1).equals(ds.findSet(2)));
        assertTrue(ds.findSet(2).equals(ds.findSet(1)));
        assertFalse(ds.findSet(1).equals(ds.findSet(3)));
        assertFalse(ds.findSet(2).equals(ds.findSet(3)));
        assertTrue(ds.getCurrentRepresentatives().size() == 4);

        ds.union(1, 3);
        assertTrue(ds.findSet(1).equals(ds.findSet(2)));
        assertTrue(ds.findSet(2).equals(ds.findSet(3)));
        assertTrue(ds.getCurrentRepresentatives().size() == 3);

        ds.union(3, 2);
        assertTrue(ds.findSet(1).equals(ds.findSet(2)));
        assertTrue(ds.findSet(2).equals(ds.findSet(3)));
        assertTrue(ds.getCurrentRepresentatives().size() == 3);

        ds.union(4, 5);
        assertTrue(ds.findSet(4).equals(ds.findSet(5)));
        assertTrue(ds.findSet(5).equals(ds.findSet(4)));
        assertTrue(ds.getCurrentRepresentatives().size() == 2);

        ds.union(4, 2);
        assertTrue(ds.findSet(1).equals(ds.findSet(2)));
        assertTrue(ds.findSet(2).equals(ds.findSet(3)));
        assertTrue(ds.findSet(3).equals(ds.findSet(4)));
        assertTrue(ds.findSet(4).equals(ds.findSet(5)));
        assertTrue(ds.getCurrentRepresentatives().size() == 1);
    }

    @Test
    final void testUnionExceptions() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        ds.makeSet(1);
        ds.makeSet(2);
        assertThrows(NullPointerException.class, () -> ds.union(null, null));
        assertThrows(NullPointerException.class, () -> ds.union(1, null));
        assertThrows(NullPointerException.class, () -> ds.union(null, 2));
        assertThrows(IllegalArgumentException.class, () -> ds.union(1, 3));
        assertThrows(IllegalArgumentException.class, () -> ds.union(3, 2));
        ds.union(1, 2);
        assertThrows(IllegalArgumentException.class, () -> ds.union(3, 1));
        ds.makeSet(3);
        assertThrows(IllegalArgumentException.class, () -> ds.union(3, 4));
        assertThrows(IllegalArgumentException.class, () -> ds.union(4, 1));
    }

    @Test
    final void testGetCurrentRepresentatives() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
        Set<Integer> controlSet = new HashSet<Integer>();
        ds.makeSet(1);
        controlSet.add(1);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));
        ds.makeSet(2);
        controlSet.add(2);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));
        ds.makeSet(3);
        controlSet.add(3);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));
        ds.makeSet(4);
        controlSet.add(4);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));
        ds.makeSet(5);
        controlSet.add(5);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.union(1, 5);
        controlSet.remove(1);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.union(3, 4);
        controlSet.remove(3);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.union(1, 3);
        controlSet.remove(5);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.makeSet(6);
        controlSet.add(6);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.union(2, 6);
        controlSet.remove(2);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));

        ds.union(1, 2);
        controlSet.remove(6);
        assertTrue(ds.getCurrentRepresentatives().equals(controlSet));
    }

    @Test
    final void testGetCurrentElementsOfSetContainingExceptions() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertThrows(NullPointerException.class,
                () -> ds.getCurrentElementsOfSetContaining(null));
        ds.makeSet(1);
        assertThrows(NullPointerException.class,
                () -> ds.getCurrentElementsOfSetContaining(null));
        assertThrows(IllegalArgumentException.class,
                () -> ds.getCurrentElementsOfSetContaining(2));
        ds.makeSet(2);
        ds.union(1, 2);
        assertThrows(IllegalArgumentException.class,
                () -> ds.getCurrentElementsOfSetContaining(3));
    }

    @Test
    final void testGetCurrentElementsOfSetContaining() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        Set<Integer> controlSet = new HashSet<Integer>();
        ds.makeSet(1);
        controlSet.add(1);
        assertTrue(ds.getCurrentElementsOfSetContaining(1).equals(controlSet));
        assertTrue(ds.getCurrentElementsOfSetContaining(1).contains(1));
        assertFalse(ds.getCurrentElementsOfSetContaining(1).contains(2));
        assertTrue(ds.getCurrentElementsOfSetContaining(1).size() == 1);
        ds.makeSet(2);
        ds.union(1, 2);
        controlSet.add(2);
        assertTrue(ds.getCurrentElementsOfSetContaining(1).equals(controlSet));
        assertTrue(ds.getCurrentElementsOfSetContaining(2).equals(controlSet));
        assertTrue(ds.getCurrentElementsOfSetContaining(1).contains(1));
        assertTrue(ds.getCurrentElementsOfSetContaining(1).contains(2));
        assertTrue(ds.getCurrentElementsOfSetContaining(2).contains(1));
        assertTrue(ds.getCurrentElementsOfSetContaining(2).contains(2));
        assertTrue(ds.getCurrentElementsOfSetContaining(1).size() == 2);
        assertTrue(ds.getCurrentElementsOfSetContaining(2).size() == 2);
        assertTrue(ds.getCurrentElementsOfSetContaining(1)
                .equals(ds.getCurrentElementsOfSetContaining(2)));
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);
        ds.makeSet(6);
        ds.union(4, 5);
        ds.union(1, 3);
        controlSet.add(3);
        assertTrue(ds.getCurrentElementsOfSetContaining(1).equals(controlSet));
        assertTrue(ds.getCurrentElementsOfSetContaining(2).equals(controlSet));
        assertTrue(ds.getCurrentElementsOfSetContaining(3).equals(controlSet));
    }

    @Test
    final void testClear() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<Integer>();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
        assertTrue(ds.currentElements.isEmpty());
        ds.makeSet(1);
        assertFalse(ds.getCurrentRepresentatives().isEmpty());
        assertFalse(ds.currentElements.isEmpty());
        ds.clear();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
        assertTrue(ds.currentElements.isEmpty());
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.union(1, 3);
        assertFalse(ds.getCurrentRepresentatives().isEmpty());
        assertFalse(ds.currentElements.isEmpty());
        ds.clear();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
        assertTrue(ds.currentElements.isEmpty());
    }
}
