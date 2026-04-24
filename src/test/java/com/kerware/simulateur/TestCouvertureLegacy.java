package com.kerware.simulateur;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kerware.simulateur.exception.DeclarantSeulException;
import com.kerware.simulateur.reusine.AdaptateurReusine;

class TestCouvertureLegacy {

	@Test
	@DisplayName("Le simulateur hérité exécute plusieurs scénarios directs")
	void testCalculImpotDirectSurPlusieursScenarios() {
		SimulateurHerite simulateur = new SimulateurHerite();

		long impotCelibataire = simulateur.calculImpot(
				20_000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
		long impotMarieNombreuxEnfants = simulateur.calculImpot(
				65_000, SituationFamiliale.MARIE, 3, 1, false);
		long impotDivorceParentIsole = simulateur.calculImpot(
				35_000, SituationFamiliale.DIVORCE, 2, 0, true);
		long impotVeufSansEnfant = simulateur.calculImpot(
				28_000, SituationFamiliale.VEUF, 0, 0, false);
		long impotVeufAvecEnfant = simulateur.calculImpot(
				28_000, SituationFamiliale.VEUF, 1, 0, false);
		long impotTresEleve = simulateur.calculImpot(
				250_000, SituationFamiliale.CELIBATAIRE, 0, 0, false);

		assertAll(
				() -> assertTrue(impotCelibataire >= 0),
				() -> assertTrue(impotMarieNombreuxEnfants >= 0),
				() -> assertTrue(impotDivorceParentIsole >= 0),
				() -> assertTrue(impotVeufSansEnfant >= 0),
				() -> assertTrue(impotVeufAvecEnfant >= 0),
				() -> assertTrue(impotTresEleve > impotCelibataire));
	}

	@Test
	@DisplayName("La méthode main du simulateur hérité reste exécutable")
	void testMainSimulateurHerite() {
		assertDoesNotThrow(() -> SimulateurHerite.main(new String[0]));
	}

	@Test
	@DisplayName("Le simulateur hérité gère le cas revenus nuls via sa façade")
	void testFacadeLegacyRevenusNuls() {
		SimulateurHerite simulateur = new SimulateurHerite();

		simulateur.setSituationsFamiliale(SituationFamiliale.CELIBATAIRE);
		simulateur.setRevenusNetDeclarant1(0);
		simulateur.setNbEnfantsACharge(0);
		simulateur.setNbEnfantsSituationHandicap(0);
		simulateur.setParentIsole(false);
		simulateur.calculImpotSurRevenuNet();

		assertAll(
				() -> assertEquals(0, simulateur.getImpotSurRevenuNet()),
				() -> assertEquals(0, simulateur.getDecote()),
				() -> assertEquals(0, simulateur.getRevenuFiscalReference()),
				() -> assertEquals(1.0, simulateur.getNbPartsFoyerFiscal()));
	}

	@Test
	@DisplayName("Le simulateur hérité retourne les valeurs attendues pour un couple")
	void testFacadeLegacyCoupleEtAbattements() throws DeclarantSeulException {
		SimulateurHerite simulateur = new SimulateurHerite();

		simulateur.setSituationsFamiliale(SituationFamiliale.MARIE);
		simulateur.setRevenusNetDeclarant1(40_000);
		simulateur.setRevenusNetDeclarant2(20_000);
		simulateur.setNbEnfantsACharge(2);
		simulateur.setNbEnfantsSituationHandicap(1);
		simulateur.setParentIsole(false);
		simulateur.calculImpotSurRevenuNet();

		assertAll(
				() -> assertEquals(4_000, simulateur.getAbattementDeclarant1()),
				() -> assertEquals(2_000, simulateur.getAbattementDeclarant2()),
				() -> assertEquals(54_000, simulateur.getRevenuFiscalReference()),
				() -> assertEquals(3.5, simulateur.getNbPartsFoyerFiscal()),
				() -> assertTrue(simulateur.getImpotSurRevenuNet() >= 0));
	}

	@Test
	@DisplayName("Le simulateur hérité couvre les branches PACSE et parent isolé")
	void testFacadeLegacyPacseEtParentIsole() throws DeclarantSeulException {
		SimulateurHerite simulateur = new SimulateurHerite();

		simulateur.setSituationsFamiliale(SituationFamiliale.PACSE);
		simulateur.setRevenusNetDeclarant1(30_000);
		simulateur.setRevenusNetDeclarant2(15_000);
		simulateur.setNbEnfantsACharge(1);
		simulateur.setNbEnfantsSituationHandicap(0);
		simulateur.setParentIsole(true);
		simulateur.calculImpotSurRevenuNet();

		assertAll(
				() -> assertEquals(2.5, simulateur.getNbPartsFoyerFiscal()),
				() -> assertEquals(40_500, simulateur.getRevenuFiscalReference()),
				() -> assertTrue(simulateur.getImpotSurRevenuNet() >= 0));
	}

	@Test
	@DisplayName("Le simulateur hérité protège le second déclarant pour un déclarant seul")
	void testFacadeLegacyExceptionsDeclarantSeul() {
		SimulateurHerite simulateur = new SimulateurHerite();

		simulateur.setSituationsFamiliale(SituationFamiliale.CELIBATAIRE);

		assertAll(
				() -> assertThrows(DeclarantSeulException.class,
						() -> simulateur.setRevenusNetDeclarant2(12_000)),
				() -> assertThrows(DeclarantSeulException.class,
						() -> simulateur.getAbattementDeclarant2()));
	}

	@Test
	@DisplayName("L'adaptateur hérité délègue correctement les scénarios principaux")
	void testAdaptateurHeriteScenarioComplet() throws DeclarantSeulException {
		AdaptateurHerite adaptateur = new AdaptateurHerite();

		adaptateur.setSituationFamiliale(SituationFamiliale.MARIE);
		adaptateur.setRevenusNetDeclarant1(40_000);
		adaptateur.setRevenusNetDeclarant2(20_000);
		adaptateur.setNbEnfantsACharge(2);
		adaptateur.setNbEnfantsSituationHandicap(1);
		adaptateur.setParentIsole(false);
		adaptateur.calculImpotSurRevenuNet();

		assertAll(
				() -> assertEquals(54_000, adaptateur.getRevenuFiscalReference()),
				() -> assertEquals(4_000, adaptateur.getAbattementDeclarant1()),
				() -> assertEquals(2_000, adaptateur.getAbattementDeclarant2()),
				() -> assertEquals(3.5, adaptateur.getNbPartsFoyerFiscal()),
				() -> assertEquals(adaptateur.getImpotSurRevenuNet() + adaptateur.getDecote(),
						adaptateur.getImpotAvantDecote()));
	}

	@Test
	@DisplayName("L'adaptateur hérité relaie les exceptions du second déclarant")
	void testAdaptateurHeriteExceptions() {
		AdaptateurHerite adaptateur = new AdaptateurHerite();

		adaptateur.setSituationFamiliale(SituationFamiliale.DIVORCE);

		assertAll(
				() -> assertThrows(DeclarantSeulException.class,
						() -> adaptateur.setRevenusNetDeclarant2(10_000)),
				() -> assertThrows(DeclarantSeulException.class,
						() -> adaptateur.getAbattementDeclarant2()));
	}

	@Test
	@DisplayName("L'adaptateur réusiné expose aussi le second abattement en couple")
	void testAdaptateurReusineAbattementDeuxiemeDeclarant() throws DeclarantSeulException {
		AdaptateurReusine adaptateur = new AdaptateurReusine();

		adaptateur.setSituationFamiliale(SituationFamiliale.PACSE);
		adaptateur.setRevenusNetDeclarant1(30_000);
		adaptateur.setRevenusNetDeclarant2(20_000);
		adaptateur.calculImpotSurRevenuNet();

		assertEquals(2_000, adaptateur.getAbattementDeclarant2());
	}
}
