package com.kerware.simulateur;

public interface ICalculateurImpot {

    public void setRevenusNetDeclarant1( int rn );
    public void setRevenusNetDeclarant2( int rn );
    public void setSituationFamiliale( SituationFamiliale sf );
    public void setNbEnfantsACharge( int nbe );
    public void setNbEnfantsSituationHandicap( int nbesh );
    public void setParentIsole( boolean pi );

    public void calculImpotSurRevenuNet();

    public int getRevenuFiscalReference();
    public int getAbattementDeclarant1();
    public int getAbattementDeclarant2();
    public double getNbPartsFoyerFiscal();
    public int getImpotAvantDecote();
    public int getDecote();
    public int getImpotSurRevenuNet();

}
