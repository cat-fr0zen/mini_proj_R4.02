package com.kerware.simulateur.reusine;

import com.kerware.simulateur.SituationFamiliale;
import com.kerware.simulateur.exception.DeclarantSeulException;

public class SimultateurReusine {
	
	/** Tranches du barème progressif (limites supérieures en euros). REF: art. 197 CGI */
    private static final int[] LIMITES_TRANCHES = { 0, 11_294, 28_797, 82_341, 177_106, Integer.MAX_VALUE };

    /** Taux marginaux associés à chaque tranche. REF: art. 197 CGI */
    private static final double[] TAUX_TRANCHES = { 0.0, 0.11, 0.30, 0.41, 0.45 };

    /** Taux de l'abattement forfaitaire pour frais professionnels. REF: art. 83 CGI */
    private static final double TAUX_ABATTEMENT = 0.10;

    /** Abattement minimum (en euros). REF: art. 83 CGI */
    private static final int ABATTEMENT_MIN = 495;

    /** Abattement maximum (en euros). REF: art. 83 CGI */
    private static final int ABATTEMENT_MAX = 14_171;

    /** Plafond de réduction d'impôt par demi-part supplémentaire (en euros). REF: art. 197 CGI */
    private static final double PLAFOND_DEMI_PART = 1_759.0;

    /** Seuil de la décote pour un déclarant seul (en euros). REF: art. 197 I-4 CGI */
    private static final double SEUIL_DECOTE_SEUL = 1_929.0;

    /** Seuil de la décote pour un couple (en euros). REF: art. 197 I-4 CGI */
    private static final double SEUIL_DECOTE_COUPLE = 3_191.0;

    /** Décote maximale pour un déclarant seul (en euros). REF: art. 197 I-4 CGI */
    private static final double DECOTE_MAX_SEUL = 873.0;

    /** Décote maximale pour un couple (en euros). REF: art. 197 I-4 CGI */
    private static final double DECOTE_MAX_COUPLE = 1_444.0;
	
    /** Le revenus net du premier déclarant */
	private int revenusNetDeclarant1 = 0;
	
	/** Le revenus net du second déclarant */
	private int revenusNetDeclarant2 = 0;
	
	/** La situation familiale du déclarant */
	private SituationFamiliale situationFamiliale;
	
	/** Le nombre d'enfants à charge */
	private int nbEnfantsACharge = 0;
	
	/** Le nombre d'enfants à charge en situation de handicap */
	private int nbEnfantsSituationHandicap = 0;
	
	/** Le statut de parent isolé du déclarant */
	private boolean estParentIsole = false;

	/**
     * Changer le revenus net du premier déclarant
     * @param revenusNetDeclarant1 le nouveu revenus net
     * @author picots
     */
	public void setRevenusNetDeclarant1(int revenusNetDeclarant1) {
		this.revenusNetDeclarant1 = revenusNetDeclarant1;
	}
	
	/**
	 * Changer le revenus net du second déclarant
	 * @param revenusNetDeclarant2 le nouveau revenus net
	 * @throws DeclarantSeulException si le premier déclarant est un parent isolé
	 * @author picots
	 */
	public void setRevenusNetDeclarant2(int revenusNetDeclarant2) throws DeclarantSeulException {
		switch(situationFamiliale) {
			case PACSE, MARIE -> this.revenusNetDeclarant2 = revenusNetDeclarant2;
			default -> throw new DeclarantSeulException();
		}
	}

	/**
	 * Changer la situation familiale
	 * @param situationFamiliale la nouvelle situation familiale
	 * @author picots
	 */
	public void setSituationsFamiliale(SituationFamiliale situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}
	
	/**
	 * Changer le nombre d'enfants à charge
	 * @param nbEnfantsACharge le nouveau nombre d'enfants à charge
	 * @author picots
	 */
	public void setNbEnfantsACharge(int nbEnfantsACharge) {
		this.nbEnfantsACharge = nbEnfantsACharge;
	}
	
	/**
	 * Changer le nombre d'enfants en situation de handicap à charge
	 * @param nbEnfantsSituationHandicap le nouveau nombre d'enfants en situation de handicap à charge
	 * @author picots
	 */
	public void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap) {
		this.nbEnfantsSituationHandicap = nbEnfantsSituationHandicap;
	}
	
	/**
	 * Changer le statut de parent isolé
	 * @param estParentIsole le nouveau statut
	 * @author picots
	 */
	public void setParentIsole(boolean estParentIsole) {
		this.estParentIsole = estParentIsole;
	}
	
	/**
	 * Calculer l'abattement à partir d'un revenus net
	 * @param revenusNet le revenus net
	 * @return l'abattement
	 */
	private int calculerAbattement(int revenusNet) {
        if (revenusNet <= 0) return 0;
        double abattement = revenusNet * TAUX_ABATTEMENT;
        abattement = Math.min(abattement, ABATTEMENT_MAX);
        abattement = Math.max(abattement, ABATTEMENT_MIN);
        return (int) Math.round(abattement);
    }

}
