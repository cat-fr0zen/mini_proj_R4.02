package com.kerware.simulateur;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.kerware.simulateur.AdaptateurHerite;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
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
}
