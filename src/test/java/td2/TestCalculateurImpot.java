package td2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kerware.simulateur.AdaptateurHerite;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
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
		calculateur.setRevenusNet(35000);
		calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
		calculateur.setNbEnfantsACharge(0);
		calculateur.setNbEnfantsSituationHandicap(0);
		calculateur.setParentIsole(false);
		
		calculateur.calculImpotSurRevenuNet();
		
		assertEquals(2736, calculateur.getImpotSurRevenuNet());
		assertEquals(1, calculateur.getNbPartsFoyerFiscal());
	}
}
