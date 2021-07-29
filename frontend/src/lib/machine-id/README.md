# machine-id

Derive a unique machine identifier from CPU, memory, and network interface information. Since the identifier is based on hardware confguration, it will be consistent across processes, but will change if hardware changes.

# Usage

`machine-id` exports a function, that returns the machine ID. Passing an argument of `true`, will return the machine ID formatted in hyphenated groups, as if it were a UUID.

```
var machineId = require('machine-id');
console.log(machineId()); // 478138e6ee1e47e8b5a717915bd48f99'
console.log(machineId(true)); // 478138e6-ee1e-47e8-b5a7-17915bd48f99
```

# Testing

Tests require [Mocha](http://visionmedia.github.com/mocha) and can be run with `npm test`.  You can specify Mocha options, such as the reporter, by adding a [mocha.opts](http://visionmedia.github.com/mocha/#mocha.opts) file to the `test` directory.

Running `npm test --coverage` will generate code coverage reports with [Istanbul](https://github.com/gotwarlost/istanbul). The code coverage reports will be located in the `coverage` directory, which is excluded from the repository.


