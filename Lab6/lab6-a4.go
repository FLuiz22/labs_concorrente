package main

import (
	"fmt";
	"math/rand"
)

func prod(in, quit chan int) {
	rand.Seed(42)

	for range rand.Intn(10000) {
		in <- rand.Intn(10)
	}

	<- quit
}

func duPar(x int) bool {
	return (x % 2 == 0)
}

func tranca(ch, quit chan int) {
	for len(quit) > 0 {

	}
	close(ch)
}

func cons(in, out chan int) {
	for x := range in {
		if duPar(x) {
			out <- x
		}
	}

	close(out)
}

func main() {
	in := make(chan int)
	pares := make(chan int, 10000)
	quit := make(chan int, 2)

	for range 2 {
		go prod(in, quit)
		quit <- 0
	}

	go tranca(in, quit)

	go cons(in, pares)

	for x := range pares {
		fmt.Printf("Par: %d\n", x)
	}
	fmt.Println("This is the end")
}