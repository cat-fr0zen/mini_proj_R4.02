package com.kerware.simulateur;

import com.kerware.simulateur.exception.DeclarantSeulException;

public interface ICalculateurImpot {

    public void setRevenusNetDeclarant1( int rn );
    public void setRevenusNetDeclarant2( int rn ) throws DeclarantSeulException;
    public void setSituationFamiliale( SituationFamiliale sf );
    public void setNbEnfantsACharge( int nbe );
    public void setNbEnfantsSituationHandicap( int nbesh );
    public void setParentIsole( boolean pi );

    public void calculImpotSurRevenuNet();

    public int getRevenuFiscalReference();
    public int getAbattementDeclarant1();
    public int getAbattementDeclarant2() throws DeclarantSeulException;
    public double getNbPartsFoyerFiscal();
    public int getImpotAvantDecote();
    public int getDecote();
    public int getImpotSurRevenuNet();

}
