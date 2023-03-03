# End of Cycle

Music Machines for SuperCollider

## Muse

Generates [Triadex Muse](https://www.till.com/articles/muse/)
compositions.

Use `Pmuse` pattern like this.

``` supercollider
/* Corresponding sliders values are shown on the left.
0    OFF  - always off
1    ON   - always on
2    C1/2 \
3    C1   |
4    C2   | binary counter
5    C4   |
6    C8   /
7    C3   \ triple counter
8    C6   /
9    B1   \
     ...  | shift register
39   B31  /
*/
(
var muse = Pmuse(9, 10, 5, 6, 0, 0, 39, 5);

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
```
