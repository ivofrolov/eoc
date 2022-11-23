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

        var theme;
        var bin_ctr_state, tri_ctr_state, sht_reg_state;
        var prev_bin_ctr_state = 1, prev_tri_ctr_state = 0, prev_sht_reg_state = 0;

        length.value(inval).do {
            theme = stateAt.value([w, x, y, z], prev_bin_ctr_state, prev_tri_ctr_state, prev_sht_reg_state);

            bin_ctr_state = binaryCounter.next;
            tri_ctr_state = if(bin_ctr_state.bitAnd(1) == 0) { tripleCounter.next } { prev_tri_ctr_state };
            sht_reg_state = if(bin_ctr_state.bitAnd(1) == 0) {
                shiftRegister.next(fourBitsParity.value(theme))
            } {
                prev_sht_reg_state
            };

            prev_bin_ctr_state = bin_ctr_state;
            prev_tri_ctr_state = tri_ctr_state;
            prev_sht_reg_state = sht_reg_state;

            stateAt.value([a, b, c, d], bin_ctr_state, tri_ctr_state, sht_reg_state).yield;
        };

        ^inval;
    }
}
