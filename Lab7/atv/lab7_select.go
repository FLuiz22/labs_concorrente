package main

import (
	"fmt"
	"time"
	"math/rand"
)

func exec(mimir int) int {
	x := time.Duration(rand.Intn(mimir)) * time.Millisecond

	fmt.Println("Eu:", x)

	time.Sleep(x)

	return int(x)
}

func aux(max_sleep int) chan int {
	ch := make(chan int)

	go func() {
		for range 1000 {
			ch <- exec(max_sleep)
		}
		close(ch)
	}()

	return ch
}

func main() {
	
	sum := 0

	ch1 := aux(rand.Intn(100))
	ch2 := aux(rand.Intn(100))

	for range 1000 {
		select {
		case y := <- ch2:
			sum += y

		case x := <- ch1:
			sum += x
		}

	}

	fmt.Println("Cabei")
	fmt.Println("A soma é a seguinte my chefe", sum)
}