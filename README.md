# End of Cycle

Music Machines for SuperCollider

## Muse

Generates [Triadex Muse](https://www.till.com/articles/muse/) compositions.

``` supercollider
Pmuse(a: 9, b: 10, c: 5, d: 6, w: 0, x: 0, y: 39, z: 5)
```

Arguments represent corresponding interval (`a`, `b`, `c`, `d`) and theme (`w`, `x`, `y`, `z`) sliders positions.

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

## Turing Machine

Generates random sequences which you can lock into loops. Like [Music Thing Modular Turing Machine](https://www.musicthing.co.uk/Turing-Machine/).

``` supercollider
Pturing(prob: 0.05, repeat: 8)
```

Degree of uncertainty is controlled by `prob` [0, 1], `repeat` [2, 16] determines loop length.


## Utilities

`Pchanged(pattern)` outputs true if next pattern value (number) changed. Can be used like this.

``` supercollider
Pif(Pchanged(Pkey(\degree)), \noteOn, Rest())
```