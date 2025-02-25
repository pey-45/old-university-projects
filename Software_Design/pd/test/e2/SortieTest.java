package e2;

import e2.Node.BranchRoute.BattleNode;
import e2.Node.BranchRoute.WaypointSpottingNode;
import e2.Node.EndNode;
import e2.Node.FixedRoute.AirRaidNode;
import e2.Node.FixedRoute.MaelstromNode;
import e2.Node.Node;
import e2.World.Fleet;
import e2.World.Sortie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SortieTest {

    static Fleet fleetA, fleetB, enemy_fleetB, enemy_fleetF, weak_fleet, inmortal_fleet;
    static Node A, B, C, D, E, F, G, H, I, J, invalid_node_branch, invalid_node_fixed, invalid_node_general, aux_air_raid_node, aux_mael_storm_node, aux_battle_node, aux_waypoint_spotting_node;
    static Sortie sortie1, sortie2, sortie_null, invalid_sortie_branch, invalid_sortie_fixed, invalid_sortie_general, sortie_sunk_fleet, sortie_survives_E;

    @BeforeAll
    public static void setup() {
        fleetA = new Fleet("Flota A", 11, 42, 47, 0, 0);
        fleetB = new Fleet("Flota B", 1, 25, 0, 46, 28);
        enemy_fleetB = new Fleet("Flota Enemiga B", 20, 30, 25, 20, 20);
        enemy_fleetF = new Fleet("Flota Enemiga F", 30, 17, 41, 20, 20);
        weak_fleet = new Fleet("Flota Débil", 1, 0, 0, 0, 0);
        inmortal_fleet = new Fleet("Flota Inmortal", 1000, 1000, 1000, 1000, 1000);
        A = new WaypointSpottingNode("A", 28);
        B = new BattleNode("B", enemy_fleetB);
        C = new MaelstromNode("C", 20);
        D = new EndNode("D");
        E = new AirRaidNode("E", 151);
        F = new BattleNode("F", enemy_fleetF);
        G = new MaelstromNode("G", 67);
        H = new EndNode("H");
        I = new EndNode("I");
        J = new EndNode("J");
        aux_mael_storm_node = new MaelstromNode("Mael Storm", 1);
        aux_air_raid_node = new AirRaidNode("Air Raid", 1);
        aux_battle_node = new BattleNode("Battle", enemy_fleetB);
        aux_waypoint_spotting_node = new WaypointSpottingNode("Waypoint Spotting", 1);

        A.setChildNode(B);
        A.setChildNode(C);
        B.setChildNode(D);
        B.setChildNode(E);
        E.setChildNode(J);
        C.setChildNode(F);
        F.setChildNode(H);
        F.setChildNode(G);
        G.setChildNode(I);
        sortie1 = new Sortie(A, fleetA);
        sortie2 = new Sortie(A, fleetB);
        sortie_survives_E = new Sortie(E, inmortal_fleet);
        sortie_null = new Sortie(null, fleetA);
        invalid_sortie_branch = new Sortie(invalid_node_branch, fleetA);
        invalid_sortie_fixed = new Sortie(invalid_node_fixed, fleetA);
        invalid_sortie_general = new Sortie(invalid_node_general, fleetA);
        sortie_sunk_fleet = new Sortie(A, weak_fleet);
    }

    @Test
    public void testToString() {
        assertEquals("""
                Flota A

                HP 11
                Blindaje 42
                Poder de fuego 47
                Antiaereo 0
                Linea de vision 0""", fleetA.toString());
        assertEquals("""
                Flota B

                HP 1
                Blindaje 25
                Poder de fuego 0
                Antiaereo 46
                Linea de vision 28""", fleetB.toString());
        assertEquals("(A WaypointSpotting, (B Battle, (D End), (E AirRaid, (J End))), (C Maelstrom, (F Battle, (H End), (G Maelstrom, (I End)))))", sortie1.toString());
        assertEquals(sortie1.toString(), sortie2.toString());
        assertThrows(IllegalArgumentException.class, () -> invalid_sortie_general.toString());
    }

    @Test
    void testSimulate() {
        assertEquals("""
                Sortie Result:
                \tSUCCESS
                \tLast Visited Node: H
                \tFinal HP: 1""", sortie1.simulate());
        assertEquals("""
                Sortie Result:
                \tFAIL
                \tLast Visited Node: E
                \tFinal HP: -33""", sortie2.simulate());
        aux_battle_node.setFleet(weak_fleet);
        aux_mael_storm_node.setFleet(weak_fleet);
        assertNull(aux_battle_node.executeNode());
        assertNull(aux_mael_storm_node.executeNode());
        assertEquals("""
                Sortie Result:
                \tSUCCESS
                \tLast Visited Node: J
                \tFinal HP: 3849""", sortie_survives_E.simulate());
    }

    @Test
    void testMinNecessaryNodes() {
        assertEquals("Smallest Node Count to End: 3", sortie1.getMinNecessaryNodes());
        assertEquals(sortie1.getMinNecessaryNodes(), sortie2.getMinNecessaryNodes());
        assertEquals("Smallest Node Count to End: 0", sortie_null.getMinNecessaryNodes());
    }

    @Test
    void testException() {
        assertThrows(IllegalArgumentException.class, () -> new Fleet("Flota_fantasma", -1, 0, 0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> A.setChildNode(F));
        assertThrows(IllegalArgumentException.class, () -> C.setChildNode(F));
        assertThrows(UnsupportedOperationException.class, () -> J.setChildNode(F));
        assertThrows(UnsupportedOperationException.class, () -> J.getChildNodes());
    }

    @Test
    void testGetNodeType() {
        assertEquals("End", J.getNodeType());
        assertEquals("Battle", aux_battle_node.getNodeType());
        assertEquals("WaypointSpotting", aux_waypoint_spotting_node.getNodeType());
        assertEquals("AirRaid", aux_air_raid_node.getNodeType());
        assertEquals("Maelstrom", aux_mael_storm_node.getNodeType());
    }
}