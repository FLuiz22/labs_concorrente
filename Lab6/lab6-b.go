package main

import (
	"fmt";
	"time";
	"math/rand"
)

func gateway(ngo int, wait_n int) int {
	ch := make(chan int)

	for range ngo {
		go func () {
			ch <- request()
		}()
	}

	sum := 0

	for range wait_n {
		sum += <- ch
	}

	return sum
}

func request() int {
	v := rand.Intn(10)
	time.Sleep(time.Duration(v) * time.Second)
	fmt.Printf("Eu: %d\n", v)

	return v
}

func main() {
	ngo := rand.Intn(10)
	var wait_n int

	if ngo > 0 {
		wait_n = rand.Intn(ngo)

		fmt.Printf("Serão criadas %d goroutines e %d serão esperadas\n", ngo, wait_n)
		fmt.Printf("Soma das %d: %d\n", wait_n, gateway(ngo, wait_n))
	} else {
		fmt.Printf("Serão criadas %d goroutines\n", ngo)
	}
}