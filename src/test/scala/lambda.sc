def identity[T]: T => T = (param: T) => param

identity("blah")
identity(0)
identity((str: String) => str.length)

// def self(func: Function1) = func(func)

def fst[T, U] = (arg1: T) => (arg2: U) => arg1

fst(1)(2)

def snd[T, U] = (arg1: T) => (arg2: U) => arg2

snd(1)(2)

// fst(identity)