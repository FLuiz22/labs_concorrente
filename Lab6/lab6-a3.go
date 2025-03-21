package main

import (
	"fmt";
	"math/rand"
)

func prod(ch, quit chan int) {
	rand.Seed(42)

	for range rand.Intn(10000) {
		ch <- rand.Intn(10)
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

func cons(ch chan int) {
	for x := range ch {
		if duPar(x) {
			fmt.Printf("Par: %d\n", x)
		}
	}
}

func main() {
	ch := make(chan int)
	quit := make(chan int, 2)

	for range 2 {
		go prod(ch, quit)
		quit <- 0
	}

	go tranca(ch, quit)

	cons(ch)
	fmt.Println("This is the end")
}