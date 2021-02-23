package io.github.durun.vstkotlin3.vst

@OptIn(ExperimentalUnsignedTypes::class)
enum class Speaker(val value: ULong) {
	L(1uL),            // Left (L)
	R(1uL shl 1),      // Right (R)
	C(1uL shl 2),      // Center (C)
	Lfe(1uL shl 3),    // Subbass (Lfe)
	Ls(1uL shl 4),     // Left Surround (Ls)
	Rs(1uL shl 5),     // Right Surround (Rs)
	Lc(1uL shl 6),     // Left of Center (Lc) - Front Left Center
	Rc(1uL shl 7),     // Right of Center (Rc) - Front Right Center
	S(1uL shl 8),      // Surround (S)
	Cs(S.value),              // Center of Surround (Cs) - Back Center - Surround (S)
	Sl(1uL shl 9),     // Side Left (Sl)
	Sr(1uL shl 10),    // Side Right (Sr)
	Tc(1uL shl 11),    // Top Center Over-head, Top Middle (Tc)
	Tfl(1uL shl 12),   // Top Front Left (Tfl)
	Tfc(1uL shl 13),   // Top Front Center (Tfc)
	Tfr(1uL shl 14),   // Top Front Right (Tfr)
	Trl(1uL shl 15),   // Top Rear/Back Left (Trl)
	Trc(1uL shl 16),   // Top Rear/Back Center (Trc)
	Trr(1uL shl 17),   // Top Rear/Back Right (Trr)
	Lfe2(1uL shl 18),  // Subbass 2 (Lfe2)
	M(1uL shl 19),     // Mono (M)

	ACN0(1uL shl 20),  // Ambisonic ACN 0
	ACN1(1uL shl 21),  // Ambisonic ACN 1
	ACN2(1uL shl 22),  // Ambisonic ACN 2
	ACN3(1uL shl 23),  // Ambisonic ACN 3
	ACN4(1uL shl 38),  // Ambisonic ACN 4
	ACN5(1uL shl 39),  // Ambisonic ACN 5
	ACN6(1uL shl 40),  // Ambisonic ACN 6
	ACN7(1uL shl 41),  // Ambisonic ACN 7
	ACN8(1uL shl 42),  // Ambisonic ACN 8
	ACN9(1uL shl 43),  // Ambisonic ACN 9
	ACN10(1uL shl 44), // Ambisonic ACN 10
	ACN11(1uL shl 45), // Ambisonic ACN 11
	ACN12(1uL shl 46), // Ambisonic ACN 12
	ACN13(1uL shl 47), // Ambisonic ACN 13
	ACN14(1uL shl 48), // Ambisonic ACN 14
	ACN15(1uL shl 49), // Ambisonic ACN 15

	Tsl(1uL shl 24),   // Top Side Left (Tsl)
	Tsr(1uL shl 25),   // Top Side Right (Tsr)
	Lcs(1uL shl 26),   // Left of Center Surround (Lcs) - Back Left Center
	Rcs(1uL shl 27),   // Right of Center Surround (Rcs) - Back Right Center

	Bfl(1uL shl 28),   // Bottom Front Left (Bfl)
	Bfc(1uL shl 29),   // Bottom Front Center (Bfc)
	Bfr(1uL shl 30),   // Bottom Front Right (Bfr)

	Pl(1uL shl 31),    // Proximity Left (Pl)
	Pr(1uL shl 32),    // Proximity Right (Pr)

	Bsl(1uL shl 33),   // Bottom Side Left (Bsl)
	Bsr(1uL shl 34),   // Bottom Side Right (Bsr)
	Brl(1uL shl 35),   // Bottom Rear Left (Brl)
	Brc(1uL shl 36),   // Bottom Rear Center (Brc)
	Brr(1uL shl 37),   // Bottom Rear Right (Brr)
}


@OptIn(ExperimentalUnsignedTypes::class)
enum class SpeakerArrangement(val str: String, val value: ULong) {
	Empty("", 0uL),          ///< empty arrangement
	Mono("Mono", Speaker.M.value),  ///< M
	Stereo("Stereo", Speaker.L.value or Speaker.R.value),    ///< L R
	StereoSurround("Stereo (Ls Rs)", Speaker.Ls.value or Speaker.Rs.value),   ///< Ls Rs
	StereoCenter("Stereo (Lc Rc)", Speaker.Lc.value or Speaker.Rc.value),   ///< Lc Rc
	StereoSide("Stereo (Sl Sr)", Speaker.Sl.value or Speaker.Sr.value),   ///< Sl Sr
	StereoCLfe("Stereo (C LFE)", Speaker.C.value or Speaker.Lfe.value),  ///< C Lfe
	StereoTF("Stereo (Tfl Tfr)", Speaker.Tfl.value or Speaker.Tfr.value),  ///< Tfl Tfr
	StereoTS("Stereo (Tsl Tsr)", Speaker.Tsl.value or Speaker.Tsr.value),  ///< Tsl Tsr
	StereoTR("Stereo (Trl Trr)", Speaker.Trl.value or Speaker.Trr.value),  ///< Trl Trr
	StereoBF("Stereo (Bfl Bfr)", Speaker.Bfl.value or Speaker.Bfr.value),  ///< Bfl Bfr
	CineFront(
		"Cine Front",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lc.value or Speaker.Rc.value
	), ///< L R C Lc Rc

	// TODO: specify val str

	/** L R C */
	Cine30("LRC", Speaker.L.value or Speaker.R.value or Speaker.C.value),

	/** L R C Lfe */
	Cine31("LRS", Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value),

	/** L R S */
	Music30("LRC+LFE", Speaker.L.value or Speaker.R.value or Speaker.Cs.value),

	/** L R Lfe S */
	Music31("LRS+LFE", Speaker.L.value or Speaker.R.value or Speaker.Lfe.value or Speaker.Cs.value),

	/** L R C   S (LCRS) */
	Cine40("LRCS", Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Cs.value),

	/** L R C   Lfe S (LCRS+Lfe) */
	Cine41("Quadro", Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Cs.value),

	/** L R Ls  Rs (Quadro) */
	Music40("LRCS+LFE", Speaker.L.value or Speaker.R.value or Speaker.Ls.value or Speaker.Rs.value),

	/** L R Lfe Ls Rs (Quadro+Lfe) */
	Music41(
		"Quadro+LFE",
		Speaker.L.value or Speaker.R.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value
	),

	/** L R C   Ls Rs */
	k50("5.0", Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value),

	/** L R C  Lfe Ls Rs */
	k51(
		"5.1",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value
	),

	/** L R C  Ls  Rs Cs */
	Cine60(
		"6.0 Cine",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value
	),

	/** L R C  Lfe Ls Rs Cs */
	Cine61(
		"6.1 Cine",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value
	),

	/** L R Ls Rs  Sl Sr */
	Music60(
		"6.0 Music",
		Speaker.L.value or Speaker.R.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R Lfe Ls  Rs Sl Sr */
	Music61(
		"6.1 Music",
		Speaker.L.value or Speaker.R.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R C   Ls  Rs Lc Rc */
	Cine70(
		"7.0 Cine (SDDS)",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value
	),

	/** L R C Lfe Ls Rs Lc Rc */
	Cine71(
		"7.1 Cine (SDDS)",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value
	),
	CineFullFront71("", Cine71.value),

	/** L R C   Ls  Rs Sl Sr */
	Music70(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R C Lfe Ls Rs Sl Sr */
	Music71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R C Lfe Ls Rs Lcs Rcs */
	CineFullRear71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lcs.value or Speaker.Rcs.value
	),
	CineSideFill71("", Music71.value),

	/** L R C Lfe Ls Rs Pl Pr */
	Proximity71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Pl.value or Speaker.Pr.value
	),

	/** L R C Ls  Rs Lc Rc Cs */
	Cine80(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Cs.value
	),

	/** L R C Lfe Ls Rs Lc Rc Cs */
	Cine81(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Cs.value
	),

	/** L R C Ls  Rs Cs Sl Sr */
	Music80(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R C Lfe Ls Rs Cs Sl Sr */
	Music81(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/** L R C Ls Rs Lc Rc Sl Sr */
	Cine90(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Sl Sr */
	Cine91(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/**.value L R C Ls Rs Lc Rc Cs Sl Sr */
	Cine100(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Cs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Cs Sl Sr */
	Cine101(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Cs.value or Speaker.Sl.value or Speaker.Sr.value
	),

	/**.value First-Order with Ambisonic Channel Number (ACN) ordering and SND3 normalization */
	AmbistOrderACN1("", Speaker.ACN0.value or Speaker.ACN1.value or Speaker.ACN2.value or Speaker.ACN3.value),

	/** Second-Order with Ambisonic Channel Number (ACN) ordering and SND3 normalization */
	AmbicdOrderACN2(
		"",
		AmbistOrderACN1.value or Speaker.ACN4.value or Speaker.ACN5.value or Speaker.ACN6.value or Speaker.ACN7.value or Speaker.ACN8.value
	),

	/** Third-Order with Ambisonic Channel Number (ACN) ordering and SND3 normalization */
	AmbirdOrderACN3(
		"",
		AmbicdOrderACN2.value or Speaker.ACN9.value or Speaker.ACN10.value or Speaker.ACN11.value or Speaker.ACN12.value or Speaker.ACN13.value or Speaker.ACN14.value or Speaker.ACN15.value
	),


/*-----------*/
/* D3 formats */
/*-----------*/
	/** L R Ls Rs Tfl Tfr Trl Trr */                        // 4.0.4
	Cube80(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/** L R C Lfe Ls Rs Cs Tc */                            // 6.1.1
	CineTopCenter71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value or Speaker.Tc.value
	),

	/** L R C Lfe Ls Rs Cs Tfc */                            // 6.1.1
	CineCenterHigh71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Cs.value or Speaker.Tfc.value
	),

	/** L R C Lfe Ls Rs Tfl Tfr */                            // 5.1.2
	CineFrontHigh71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfr.value
	),
	MPEG71D3("", CineFrontHigh71.value),

	/** L R C Lfe Ls Rs Tsl Tsr */                            // 5.1.2
	CineSideHigh71(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/** L R Lfe Ls Rs Tfl Tfc Tfr Bfc */                    // 4.1.4
	MPEG81D(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Bfc.value
	),

	/**.value L R C Ls Rs Tfl Tfr Trl Trr */                        // 5.0.4
	k90(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),
	k50_4("", k90.value),

	/** L R C Lfe Ls Rs Tfl Tfr Trl Trr */                    // 5.1.4
	k91(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),
	k51_4("", k91.value),

	/** L R C Ls Rs Sl Sr Tsl Tsr */                        // 7.0.2
	k70_2(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/**.value L R C Lfe Ls Rs Sl Sr Tsl Tsr */                    // 7.1.2
	k71_2(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),
	Atmos91("", k71_2.value),        // 9.1 Dolby Atmos (D3)

	/** L R C Ls Rs Sl Sr Tfl Tfr Trl Trr */                // 7.0.4
	k70_4(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Sl Sr Tfl Tfr Trl Trr */            // 7.1.4
	k71_4(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),
	MPEG111D3("", k71_4.value),

	/** L R C Ls Rs Sl Sr Tfl Tfr Trl Trr Tsl Tsr */        // 7.0.6
	k70_6(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/**.value L R C Lfe Ls Rs Sl Sr Tfl Tfr Trl Trr Tsl Tsr */    // 7.1.6
	k71_6(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/**.value L R C Ls Rs Lc Rc Sl Sr Tfl Tfr Trl Trr */            // 9.0.4
	k90_4(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Sl Sr Tfl Tfr Trl Trr */        // 9.1.4
	k91_4(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Sl Sr Tfl Tfr Trl Trr Tsl Tsr */ // 9.0.6
	k90_6(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Sl Sr Tfl Tfr Trl Trr Tsl Tsr */ // 9.1.6
	k91_6(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Tsl.value or Speaker.Tsr.value
	),

	/**.value L R C Ls Rs Tc Tfl Tfr Trl Trr */                    // 5.0.5
	k100(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Tc Tfl Tfr Trl Trr */                // 5.1.5
	k101(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),
	MPEG101D3("", k101.value),

	/** L R C Lfe Ls Rs Tfl Tfc Tfr Trl Trr Lfe2 */            // 5.2.5
	k102(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Lfe2.value
	),

	/**.value L R C Ls Rs Tc Tfl Tfc Tfr Trl Trr */                // 5.0.6
	k110(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Tc Tfl Tfc Tfr Trl Trr */            // 5.1.6
	k111(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Tfl Tfc Tfr Trl Trr Lfe2 */    // 7.2.5
	k122(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Lfe2.value
	),

	/**.value L R C Ls Rs Sl Sr Tc Tfl Tfc Tfr Trl Trr */            // 7.0.6
	k130(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R C Lfe Ls Rs Sl Sr Tc Tfl Tfc Tfr Trl Trr */        // 7.1.6
	k131(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value
	),

	/**.value L R Ls Rs Sl Sr Tfl Tfr Trl Trr Bfl Bfr Brl Brr  */    // 6.0.4.4
	k140(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tfl.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trr.value or Speaker.Bfl.value or Speaker.Bfr.value or Speaker.Brl.value or Speaker.Brr.value
	),

	/**.value L R C Lfe Ls Rs Lc Rc Cs Sl Sr Tc Tfl Tfc Tfr Trl Trc Trr Lfe2 Tsl Tsr Bfl Bfc Bfr */    // 10.2.9.3
	k222(
		"",
		Speaker.L.value or Speaker.R.value or Speaker.C.value or Speaker.Lfe.value or Speaker.Ls.value or Speaker.Rs.value or Speaker.Lc.value or Speaker.Rc.value or Speaker.Cs.value or Speaker.Sl.value or Speaker.Sr.value or Speaker.Tc.value or Speaker.Tfl.value or Speaker.Tfc.value or Speaker.Tfr.value or Speaker.Trl.value or Speaker.Trc.value or Speaker.Trr.value or Speaker.Lfe2.value or Speaker.Tsl.value or Speaker.Tsr.value or Speaker.Bfl.value or Speaker.Bfc.value or Speaker.Bfr.value
	);

	companion object {
		private val valueMap = values().associateBy { it.value }
		fun of(value: ULong): SpeakerArrangement {
			return valueMap[value]
				?: throw IllegalArgumentException()
		}
	}
}