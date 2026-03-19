package com.kerware.simulateur.reusine;

import com.kerware.simulateur.SituationFamiliale;
import com.kerware.simulateur.exception.DeclarantSeulException;

/**
 * Le nouveau simulateur avec un code testable
 * @author picots
 */
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
    
    /** Taux de calcul de la décote. REF: art. 197 I-4 CGI */
    private static final double TAUX_DECOTE = 0.4525;
	
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
	
	/** L'abattement du premier déclarant */
	private int abattementDeclarant1 = 0;
	
	/** L'abattement de second déclarant */
    private int abattementDeclarant2 = 0;
    
    /** Le revenus fiscal de référence */
    private int revenuFiscalReference = 0;
    
    /** Le nombre de parts du foyer fiscal */
    private double nbPartsFoyerFiscal = 0.0;
    
    /** La décôte */
    private int decote = 0;
    
    /** L'impôt sur le revenus net */
    private int impotNet = 0;

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

	/**
	 * Obtenir le revenus fiscal de référence
	 * @return le revenus fiscal de référence
	 * @author picots
	 */
	public int getRevenuFiscalReference() {
		return revenuFiscalReference;
	}
	
	/**
	 * Obtenir l'abattement du premier déclarant
	 * @return l'abattement du premier déclarant
	 * @author picots
	 */
	public int getAbattementDeclarant1() {
		return abattementDeclarant1;
	}
	
	/**
	 * Obtenir l'abattement du second déclarant
	 * @return l'abattement du second déclarant
	 * @author picots
	 */
	public int getAbattementDeclarant2() throws DeclarantSeulException{
		switch (situationFamiliale) {
			case PACSE, MARIE :
				return abattementDeclarant2;
			default :
				throw new DeclarantSeulException();
		}
	}
	
	/**
	 * Obtenir le nombre de parts du foyer fiscal
	 * @return le nombre de parts du foyer fiscal
	 * @author picots
	 */
	public double getNbPartsFoyerFiscal() {
		return nbPartsFoyerFiscal;
	}
	
	/**
	 * Obtenir la decote
	 * @return la decote
	 * @author picots
	 */
	public int getDecote() {
		return decote;
	}
	
	/**
	 * Obtenir l'impôt sur le revenu net
	 * @return l'impôt sur le revenu net
	 * @author picots
	 */
	public int getImpotSurRevenuNet() {
		return impotNet;
	}
	
	/**
     * Calcule la décote applicable à l'impôt.
     * @param impot impôt avant décote
     * @param nbPartsDeclarants nombre de parts des déclarants (1 = seul, 2 = couple)
     * @return montant de la décote (plafonné à l'impôt lui-même)
     * @author picots
     */
    private int calculerDecote(int impot, double nbPartsDeclarants) {
        if (impot <= 0) return 0;

        double decoteCalculee = 0.0;

        if (nbPartsDeclarants == 1 && impot < SEUIL_DECOTE_SEUL) {
            decoteCalculee = DECOTE_MAX_SEUL - (impot * TAUX_DECOTE);
        } else if (nbPartsDeclarants == 2 && impot < SEUIL_DECOTE_COUPLE) {
            decoteCalculee = DECOTE_MAX_COUPLE - (impot * TAUX_DECOTE);
        }

        decoteCalculee = Math.round(decoteCalculee);
        // La décote ne peut pas dépasser l'impôt lui-même
        return (int) Math.min(decoteCalculee, impot);
    }
    
    /**Détermine le nombre de parts attribuées aux déclarants selon leur situation familiale.
    * @return nombre de parts des déclarants
    * @author picots 
    */
    private double calculerPartsDeclarants() {
        return switch (situationFamiliale) {
            case MARIE, PACSE -> 2.0;
            case CELIBATAIRE, DIVORCE, VEUF -> 1.0;
        };
    }
    	
    /**
     * Calcule les demi-parts supplémentaires liées aux enfants à charge.
     * @return demi-parts enfants
     * @author picots
     */
    private double calculerPartsEnfants() {
        if (nbEnfantsACharge <= 0) return 0.0;
        if (nbEnfantsACharge <= 2) {
            return nbEnfantsACharge * 0.5;
        }
        return 1.0 + (nbEnfantsACharge - 2);
    }
    
    /**
     * Calcule la demi-part supplémentaire pour parent isolé (case T).
     * @return 0.5 si parent isolé avec enfant(s), sinon 0
     * @author picots
     */
    private double calculerPartsParentIsole() {
        if (estParentIsole && nbEnfantsACharge > 0) {
            return 0.5;
        }
        return 0.0;
    }
    
    /**
     * Calcule les demi-parts supplémentaires pour enfants en situation de handicap.
     * @return demi-parts enfants handicapés
     * @author picots
     */
    private double calculerPartsEnfantsHandicapes() {
        return nbEnfantsSituationHandicap * 0.5;
    }
}
