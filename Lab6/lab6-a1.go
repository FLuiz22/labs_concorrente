package main

import (
	"fmt";
	"math/rand"
)

func prod(ch chan int) {
	rand.Seed(42)

	for {
		ch <- rand.Intn(10)
	}
}

func duPar(x int) bool {
	return (x % 2 == 0)
}

func cons(ch chan int) {
	for {
		x := <- ch
	
		if duPar(x) {
			fmt.Printf("Par: %d\n", x)
		}
	}
}

func main() {
	ch := make(chan int)

	go prod(ch)
	cons(ch)
}