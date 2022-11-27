(
MIDIClient.init;
~midiOut = MIDIOut.newByName("IAC Driver", "IAC Bus 1");

TempoClock.default.tempo = 84/60;
)

/* State has the following structure (or bits sequence,
   f.e. 01111001010~0). Corresponding sliders values
   are shown on the left.

0    OFF  □ - always 0
1    ON   ■ - always 1
2    C1/2 ■ \
3    C1   ■ |
4    C2   ■ | binary counter
5    C4   □ |
6    C8   □ /
7    C3   ■ \ triple counter
8    C6   □ /
9    B1   ■ \
     ...  □ | shift register
39   B31  □ /
*/

(
Pdefn(\a, 9);
Pdefn(\b, 10);
Pdefn(\c, 5);
Pdefn(\d, 6);
Pdefn(\w, 0);
Pdefn(\x, 0);
Pdefn(\y, 39);
Pdefn(\z, 5);
)

(
var muse;

muse = Pmuse(
    Pdefn(\a),
    Pdefn(\b),
    Pdefn(\c),
    Pdefn(\d),
    Pdefn(\w),
    Pdefn(\x),
    Pdefn(\y),
    Pdefn(\z),
);

Pbind(
    \type, \midi,
    \midiout, ~midiOut,
    \chan, 0,
    \scale, [0, 2, 4, 5, 7, 9, 11],
    \root, 0,
    \degree, muse,
    \amp, 0.7,
    \dur, 0.25,
    \legato, 1.5,
    \midicmd, Pif(Pchanged(Pkey(\degree)), \noteOn, Rest()),
).play;
)
