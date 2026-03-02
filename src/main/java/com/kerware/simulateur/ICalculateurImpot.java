package com.kerware.simulateur;

import com.kerware.simulateur.exception.DeclarantSeulException;

public interface ICalculateurImpot {

    public void setRevenusNetDeclarant1( int revenusNetDeclarant1);
    public void setRevenusNetDeclarant2( int revenusNetDeclarant2 ) throws DeclarantSeulException;
    public void setSituationFamiliale( SituationFamiliale situationFamiliale );
    public void setNbEnfantsACharge( int nbEnfantsACharge );
    public void setNbEnfantsSituationHandicap( int nbEnfantsSituationHandicap );
    public void setParentIsole( boolean estParentIsole );

    public void calculImpotSurRevenuNet();

    public int getRevenuFiscalReference();
    public int getAbattementDeclarant1();
    public int getAbattementDeclarant2() throws DeclarantSeulException;
    public double getNbPartsFoyerFiscal();
    public int getImpotAvantDecote();
    public int getDecote();
    public int getImpotSurRevenuNet();

}
