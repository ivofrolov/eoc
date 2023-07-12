# End of Cycle

Music Machines for SuperCollider

## Muse

Generates [Triadex Muse](https://www.till.com/articles/muse/) compositions.

``` supercollider
Pmuse(a: 9, b: 10, c: 5, d: 6, w: 0, x: 0, y: 39, z: 5)
//    \      interval       /  \        theme        /
```

Arguments represent corresponding sliders positions.

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
