[![Download](https://api.bintray.com/packages/novacrypto/BIP/BIP32derivation/images/download.svg)](https://bintray.com/novacrypto/BIP/BIP32derivation/_latestVersion) [![Build Status](https://travis-ci.org/NovaCrypto/BIP32Derivation.svg?branch=master)](https://travis-ci.org/NovaCrypto/BIP32Derivation) [![codecov](https://codecov.io/gh/NovaCrypto/BIP32derivation/branch/master/graph/badge.svg)](https://codecov.io/gh/NovaCrypto/BIP32derivation)

# Install

Use either of these repositories:

```
repositories {
    jcenter()
}
```
(coming soon)

Or:

```
repositories {
    maven {
        url 'https://dl.bintray.com/novacrypto/BIP/'
    }
}
```

Add dependency:

```
dependencies {
    compile 'io.github.novacrypto:BIP32derivation:2018.10.06@jar'
}

```

# Usage

```
Derive<YourKeyType> derive = new CkdFunctionDerive<>((parent, childIndex) -> {/*your CKD function*/}, yourRootKey);
YourKeyType ketAtPath = derive.derive("m/44'/0'/0'/0/0");
```

## With own path type

If you don't want to use strings to describe paths.

```
Derive<YourKeyType> derive = new CkdFunctionDerive<>((parent, childIndex) -> {/*your CKD function*/}, yourRootKey);
YourKeyType ketAtPath = derive.derive(YourCustomPathType, YourCustomDeriveImplementation);
```

## Caching

```
CkdFunction<YourKeyType> ckd = (parent, childIndex) -> {/*your CKD function*/};
CkdFunction<YourKeyType> ckdWithCache = CkdFunctionResultCacheDecorator.newCacheOf(ckd);
Derive<YourKeyType> derive = new CkdFunctionDerive<>(ckdWithCache, yourRootKey);
YourKeyType ketAtPath1 = derive.derive("m/44'/0'/0'/0/0");
YourKeyType ketAtPath2 = derive.derive("m/44'/0'/0'/0/0");
assertSame(ketAtPath1, ketAtPath2);
```

Note you can use [NovaCrypto/BIP44](https://github.com/NovaCrypto/BIP44) to form the path with a fluent syntax.
