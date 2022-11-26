Pmuse : Pattern {
    var <>a, <>b, <>c, <>d, <>w, <>x, <>y, <>z, <>length;

    *new { |a=0, b=0, c=0, d=0, w=0, x=0, y=0, z=0, length=inf|
        ^super.newCopyArgs(a, b, c, d, w, x, y, z, length)
    }

    storeArgs { ^[a, b, c, d, w, x, y, z, length] }

    embedInStream { |inval|
        var fromBinaryDigits = { |anArray|
            var num = 0;
            anArray.reverseDo({ |bit| num = num.leftShift.bitOr(bit) });
            num
        };

        var bitsParity = { |bits, aNumber|
            var y = 1;
            bits.do { |num| y = y.bitXor(aNumber.rightShift(num)) };
            y.bitAnd(1)
        },
        fourBitsParity = bitsParity.value(4, _);

        var binaryCounter = Routine.new({
            var ctr = 1;
            loop {
                ctr = ctr + 1;
                ctr.bitAnd((2**5 - 1).asInteger).yield;
            }
        });

        var tripleCounter = Routine.new({
            var ctr = 0;
            loop {
                ctr = if(((ctr + 1) & 3) == 2) { ctr + 2 } {ctr + 1 };
                ctr.rightShift(2).bitAnd((2**2 - 1).asInteger).yield;
            }
        });

        var shiftRegister = Routine.new({ |feed|
            var reg = 0;
            loop {
                reg = ((reg << 1) | (feed & 1));
                feed = reg.bitAnd((2**31 - 1).asInteger).yield;
            }
        });

        var stateAt = { |positions, bin_ctr_state, tri_ctr_state, sht_reg_state|
            var bits = positions.collect({ |pos|
                case
                { pos >= 9 } { sht_reg_state.rightShift(pos - 9).bitAnd(1) }
                { pos >= 7 } { tri_ctr_state.rightShift(pos - 7).bitAnd(1) }
                { pos >= 2 } { bin_ctr_state.rightShift(pos - 2).bitAnd(1) }
                { pos == 1 } { 1 }
                { pos == 0 } { 0 }
            });
            fromBinaryDigits.value(bits)
        };

        var aStr = a.asStream;
        var bStr = b.asStream;
        var cStr = c.asStream;
        var dStr = d.asStream;
        var wStr = w.asStream;
        var xStr = x.asStream;
        var yStr = y.asStream;
        var zStr = z.asStream;
        var aVal, bVal, cVal, dVal, wVal, xVal, yVal, zVal;

        var theme, interval;
        var bin_ctr_state = 1, tri_ctr_state = 0, sht_reg_state = 0;

        length.value(inval).do {
            aVal = aStr.next(inval);
            bVal = bStr.next(inval);
            cVal = cStr.next(inval);
            dVal = dStr.next(inval);
            wVal = wStr.next(inval);
            xVal = xStr.next(inval);
            yVal = yStr.next(inval);
            zVal = zStr.next(inval);

            theme = stateAt.value(
                [wVal, xVal, yVal, zVal],
                bin_ctr_state,
                tri_ctr_state,
                sht_reg_state
            );

            bin_ctr_state = binaryCounter.next;
            if(bin_ctr_state.bitAnd(1) == 0) {
                tri_ctr_state = tripleCounter.next;
                sht_reg_state = shiftRegister.next(fourBitsParity.value(theme));
            };

            interval = stateAt.value(
                [aVal, bVal, cVal, dVal],
                bin_ctr_state,
                tri_ctr_state,
                sht_reg_state
            );
            interval.yield;
        };

        ^inval
    }
}

Pchanged : FilterPattern {
	embedInStream { |inval|
		var stream = pattern.asStream;
		var next, prev = stream.next(inval);
        inval = true.yield;
		while {
			next = stream.next(inval);
			next.notNil;
		}{
            inval = (next - prev).sign.perform('!=', 0).yield;
			prev = next;
		}
		^inval
	}
}
