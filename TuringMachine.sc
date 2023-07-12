Pturing : Pattern {
	classvar bits = 16;

	var <>prob, <>repeat, <>length;

	*new { |prob=1.0, repeat=8, length=inf|
		^super.newCopyArgs(prob, repeat, length)
	}

	storeArgs { ^[prob, repeat, length] }

	embedInStream { |inval|
		var probStr = prob.asStream;
		var repeatStr = repeat.asStream;
		var probVal, repeatVal;

		var reg = 0.rrand((2**bits - 1).asInteger);

		length.value(inval).do {
			reg.yield;

			probVal = probStr.next(inval).clip2(1);
			repeatVal = repeatStr.next(inval).clip2(bits);

			// feedback N-th bit
			reg = ((reg >> 1) | ((reg << (repeatVal - 1)) & (1 << (bits - 1))));
			// maybe flip most significant bit
			if(prob.coin) {
				reg = reg.setBit((bits - 1), 0.5.coin);
			}
		};

		^inval
	}
}
