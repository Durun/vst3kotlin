package io.github.durun.vst3kotlin.pluginterface.vst

@ExperimentalUnsignedTypes
enum class Speaker (val value: ULong) {
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