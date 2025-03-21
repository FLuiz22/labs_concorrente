package main

import (
	"fmt";
	"math/rand"
)

func prod(ch chan int) {
	rand.Seed(42)

	for range 10000 {
		ch <- rand.Intn(10)
	}
}

func duPar(x int) bool {
	return (x % 2 == 0)
}

func cons(ch chan int) {
	for range 10000 {
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
	fmt.Println("This is the end")
}