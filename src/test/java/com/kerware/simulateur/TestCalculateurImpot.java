package com.kerware.simulateur;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.kerware.simulateur.exception.DeclarantSeulException;
import com.kerware.simulateur.reusine.AdaptateurReusine;

class TestCalculateurImpot {
	
	static final int CODE_HERITE = 1;
	
	static final int CODE_REUSINE = 2;
	
	static final int CODE = CODE_HERITE;
	
	static ICalculateurImpot calculateur;
	
	@BeforeAll
	public static void prepareCalculateur() {
		if(CODE == CODE_HERITE) calculateur = new AdaptateurHerite();
		else calculateur = new AdaptateurReusine();
	}
	
	@Test
	@DisplayName("Test du calcul de l'impôt pour un célibataire sans enfants")
	public void testImpotSurRevenuNetPourUnCelibataireSansEnfants() {
		calculateur.setRevenusNetDeclarant1(35000);
		calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
		calculateur.setNbEnfantsACharge(0);
		calculateur.setNbEnfantsSituationHandicap(0);
		calculateur.setParentIsole(false);
		
		calculateur.calculImpotSurRevenuNet();
		
		assertEquals(2736, calculateur.getImpotSurRevenuNet());
		assertEquals(1, calculateur.getNbPartsFoyerFiscal());
	}
	
	@Test
	@DisplayName("Test des setters de revenus net pour un déclarant seul")
	public void testSituationsDeclarantSeul() {
		
            assertDoesNotThrow(() -> {
                calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
                calculateur.setRevenusNetDeclarant1(30_000);
                calculateur.calculImpotSurRevenuNet();
            });
            
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            assertThrows(DeclarantSeulException.class,() -> calculateur.setRevenusNetDeclarant2(20_000));
            calculateur.setSituationFamiliale(SituationFamiliale.DIVORCE);
            assertThrows(DeclarantSeulException.class,() -> calculateur.setRevenusNetDeclarant2(20_000));

            calculateur.setSituationFamiliale(SituationFamiliale.VEUF);
            assertThrows(DeclarantSeulException.class,() -> calculateur.setRevenusNetDeclarant2(20_000));
        
	}
	
	@Test
    @DisplayName("Test des setters de revenus net pour deux déclarants")
    public void testSituationsDeuxDeclarants() {
        
		assertDoesNotThrow(() -> {
            calculateur.setSituationFamiliale(SituationFamiliale.MARIE);
            calculateur.setRevenusNetDeclarant1(30_000);
            calculateur.setRevenusNetDeclarant2(20_000);
            calculateur.calculImpotSurRevenuNet();
        });
        
        assertDoesNotThrow(() -> {
            calculateur.setSituationFamiliale(SituationFamiliale.PACSE);
            calculateur.setRevenusNetDeclarant1(30_000);
            calculateur.setRevenusNetDeclarant2(15_000);
            calculateur.calculImpotSurRevenuNet();
        });
    }
	
	@Test
    @DisplayName("Test du deuxième abattement en mode CELIBATAIRE")
    void testDeuxiemeAbattementCelibataire() {
        calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
        calculateur.setRevenusNetDeclarant1(30_000);
        calculateur.calculImpotSurRevenuNet();
        assertThrows(DeclarantSeulException.class,
                () -> calculateur.getAbattementDeclarant2());
    }
	
	@Nested
    @DisplayName("Calcul de l'abattement")
    class Abattement {

        @Test
        @DisplayName("Revenus très faibles → abattement minimum (495€)")
        void testAbattementRevenusMinimum() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(4_000); // 10% = 400 < 495
            calculateur.calculImpotSurRevenuNet();
            assertEquals(495, calculateur.getAbattementDeclarant1());
        }

        @Test
        @DisplayName("Revenus élevés → abattement maximum (14 171€)")
        void testAbattementRevenusMaximum() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(200_000); // 10% = 20 000 > 14 171
            calculateur.calculImpotSurRevenuNet();
            assertEquals(14_171, calculateur.getAbattementDeclarant1());
        }

        @Test
        @DisplayName("Revenus médians → abattement à 10%")
        void testAbattementRevenusMedians() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(30_000); // 10% = 3 000
            calculateur.calculImpotSurRevenuNet();
            assertEquals(3_000, calculateur.getAbattementDeclarant1());
        }
    }
	
	@Nested
    @DisplayName("Revenu fiscal de référence")
    class RevenuFiscalReference {

        @Test
        @DisplayName("Célibataire – RFR = revenu net – abattement")
        void testCelibataire() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(30_000);
            calculateur.calculImpotSurRevenuNet();
            // 30 000 - 3 000 = 27 000
            assertEquals(27_000, calculateur.getRevenuFiscalReference());
        }

        @Test
        @DisplayName("Couple marié – RFR = somme des revenus nets – abattements")
        void testCouple() throws DeclarantSeulException {
            calculateur.setSituationFamiliale(SituationFamiliale.MARIE);
            calculateur.setRevenusNetDeclarant1(40_000); // abatt. 4 000
            calculateur.setRevenusNetDeclarant2(20_000); // abatt. 2 000
            calculateur.calculImpotSurRevenuNet();
            // (40 000 - 4 000) + (20 000 - 2 000) = 54 000
            assertEquals(54_000, calculateur.getRevenuFiscalReference());
        }

        @Test
        @DisplayName("Revenus nuls → RFR nul ou 0")
        void testRevenusNuls() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(0);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(0, calculateur.getRevenuFiscalReference());
        }
    }
	
	@Nested
    @DisplayName("Calcul du nombre de parts du foyer fiscal")
    class NbParts {

        @Test
        @DisplayName("Célibataire sans enfant → 1 part")
        void testCelibataireSansEnfant() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(1.0, calculateur.getNbPartsFoyerFiscal());
        }

        @Test
        @DisplayName("Marié sans enfant → 2 parts")
        void testMarieSansEnfant() throws DeclarantSeulException {
            calculateur.setSituationFamiliale(SituationFamiliale.MARIE);
            calculateur.setRevenusNetDeclarant1(30_000);
            calculateur.setRevenusNetDeclarant2(20_000);
            calculateur.setNbEnfantsACharge(0);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(2.0, calculateur.getNbPartsFoyerFiscal());
        }

        @Test
        @DisplayName("Marié avec 2 enfants → 3 parts")
        void testMarieDeuxEnfants() throws DeclarantSeulException {
            calculateur.setSituationFamiliale(SituationFamiliale.MARIE);
            calculateur.setRevenusNetDeclarant1(40_000);
            calculateur.setRevenusNetDeclarant2(20_000);
            calculateur.setNbEnfantsACharge(2);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(3.0, calculateur.getNbPartsFoyerFiscal());
        }

        @Test
        @DisplayName("Célibataire, parent isolé, 1 enfant → 2 parts")
        void testParentIsole1Enfant() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(1);
            calculateur.setParentIsole(true);
            calculateur.calculImpotSurRevenuNet();
            // 1 (célibataire) + 0.5 (enfant) + 0.5 (parent isolé) = 2
            assertEquals(2.0, calculateur.getNbPartsFoyerFiscal());
        }

        @Test
        @DisplayName("Marié, 2 enfants dont 1 handicapé → 3.5 parts")
        void testMarieEnfantHandicape() throws DeclarantSeulException {
            calculateur.setSituationFamiliale(SituationFamiliale.MARIE);
            calculateur.setRevenusNetDeclarant1(40_000);
            calculateur.setRevenusNetDeclarant2(20_000);
            calculateur.setNbEnfantsACharge(2);
            calculateur.setNbEnfantsSituationHandicap(1);
            calculateur.calculImpotSurRevenuNet();
            // 2 + 1 (2 enfants) + 0.5 (handicap) = 3.5
            assertEquals(3.5, calculateur.getNbPartsFoyerFiscal());
        }

        @Test
        @DisplayName("Célibataire, 3 enfants → 2.5 parts")
        void testCelibataireTroisEnfants() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(3);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(2.5, calculateur.getNbPartsFoyerFiscal());
        }
    }
	
	@Nested
    @DisplayName("Impôt brut avant décote")
    class ImpotAvantDecote {

        @Test
        @DisplayName("Revenus très faibles → impôt avant décote = 0")
        void testImpotBrutRevenuNonImposable() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(10_000);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(0, calculateur.getImpotAvantDecote());
        }

        @Test
        @DisplayName("Revenu dans la tranche à 11% → impôt cohérent")
        void testImpotBrutTranche11Pourcent() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(20_000);
            calculateur.calculImpotSurRevenuNet();
            int impot = calculateur.getImpotAvantDecote();
            assertTrue(impot > 0, "L'impôt doit être positif pour ce revenu");
            assertTrue(impot < 2_000, "L'impôt doit rester dans un ordre de grandeur cohérent");
        }

        @Test
        @DisplayName("Revenu dans la tranche à 30% → impôt plus élevé")
        void testImpotBrutTranche30Pourcent() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(60_000); // RFR = 54 000
            calculateur.calculImpotSurRevenuNet();
            int impot = calculateur.getImpotAvantDecote();
            assertTrue(impot > 5_000, "Impôt attendu > 5 000€ pour 60 000€ de revenus");
        }
    }
	
	@Nested
    @DisplayName("Décote")
    class Decote {

        @Test
        @DisplayName("Impôt faible → décote positive")
        void testDecoteImpotFaible() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(18_000);
            calculateur.calculImpotSurRevenuNet();
            assertTrue(calculateur.getDecote() > 0, "Une décote est attendue pour un impôt modeste");
        }

        @Test
        @DisplayName("Impôt élevé (> seuil décote) → décote nulle")
        void testDecoteImpotEleve() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(80_000);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(0, calculateur.getDecote(), "Aucune décote pour un impôt élevé");
        }

        @Test
        @DisplayName("Décote ne peut pas rendre l'impôt négatif")
        void testDecoteNePasDepasserImpotAvantDecote() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(15_000);
            calculateur.calculImpotSurRevenuNet();
            assertTrue(calculateur.getDecote() <= calculateur.getImpotAvantDecote(),
                    "La décote ne doit pas excéder l'impôt avant décote");
        }

        @Test
        @DisplayName("Revenus nuls → décote nulle (pas d'impôt à décôter)")
        void testDecoteRevenusNuls() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(0);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(0, calculateur.getDecote());
        }
    }
	
	@Nested
    @DisplayName("Impôt net sur le revenu")
    class ImpotNet {

        @Test
        @DisplayName("Impôt net = impôt avant décote − décote")
        void testImpotNetEgalImpotBrutMoinsDecote() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(25_000);
            calculateur.calculImpotSurRevenuNet();
            int attendu = calculateur.getImpotAvantDecote() - calculateur.getDecote();
            assertEquals(attendu, calculateur.getImpotSurRevenuNet());
        }

        @Test
        @DisplayName("Impôt net jamais négatif")
        void testImpotNetJamaisNegatif() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(5_000);
            calculateur.calculImpotSurRevenuNet();
            assertTrue(calculateur.getImpotSurRevenuNet() >= 0,
                    "L'impôt net ne peut pas être négatif");
        }

        @Test
        @DisplayName("Revenus non imposables → impôt net nul")
        void testImpotNetRevenuNonImposable() {
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setRevenusNetDeclarant1(10_000);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(0, calculateur.getImpotSurRevenuNet());
        }

        @Test
        @DisplayName("Ajout d'enfants réduit l'impôt net (quotient familial)")
        void testImpotNetEnfantsReduisentImpot() {
            // Sans enfant
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);
            calculateur.setRevenusNetDeclarant1(50_000);
            calculateur.calculImpotSurRevenuNet();
            int impotSansEnfant = calculateur.getImpotSurRevenuNet();

            // Avec 2 enfants
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(2);
            calculateur.setRevenusNetDeclarant1(50_000);
            calculateur.calculImpotSurRevenuNet();
            int impotAvecEnfants = calculateur.getImpotSurRevenuNet();

            assertTrue(impotAvecEnfants < impotSansEnfant,
                    "Les enfants à charge doivent réduire l'impôt");
        }

        @Test
        @DisplayName("Parent isolé bénéficie d'une part supplémentaire → impôt réduit")
        void testImpotNetParentIsoleBeneficePartSupp() {
            // Parent NON isolé
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(1);
            calculateur.setParentIsole(false);
            calculateur.setRevenusNetDeclarant1(40_000);
            calculateur.calculImpotSurRevenuNet();
            int impotNonIsole = calculateur.getImpotSurRevenuNet();

            // Parent isolé mêmes revenus
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(1);
            calculateur.setParentIsole(true);
            calculateur.setRevenusNetDeclarant1(40_000);
            calculateur.calculImpotSurRevenuNet();
            int impotIsole = calculateur.getImpotSurRevenuNet();

            assertTrue(impotIsole <= impotNonIsole,
                    "Le statut de parent isolé doit diminuer ou maintenir l'impôt");
        }
    }
}
