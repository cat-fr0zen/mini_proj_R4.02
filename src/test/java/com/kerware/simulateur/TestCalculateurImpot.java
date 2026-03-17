package com.kerware.simulateur;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
}
