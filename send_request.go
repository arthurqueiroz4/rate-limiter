package main

import (
	"fmt"
	"net/http"
	"sync"
)

var toomany int

func sendRequest(url string, requestNumber int, wg *sync.WaitGroup) {
	defer wg.Done()
	resp, err := http.Get(url)
	if err != nil {
		fmt.Printf("Request %d failed: %v\n", requestNumber, err)
		return
	}

	if resp.StatusCode == 429 {
		toomany++
	}
}

func main() {
	url := "http://10.200.200.149:8000/"
	numRequests := 20

	var wg sync.WaitGroup

	for i := 1; i <= numRequests; i++ {
		wg.Add(1)
		go sendRequest(url, i, &wg)
	}

	wg.Wait()

	fmt.Println(toomany)
}
