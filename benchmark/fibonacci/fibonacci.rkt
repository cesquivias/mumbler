#lang racket/base

(define start (current-milliseconds))

(define (fibonacci n)
  (if (< n 2)
      1
      (+ (fibonacci (- n 1))
         (fibonacci (- n 2)))))
(fibonacci 30)

(define end (current-milliseconds))

(printf "computation time: ~a\n" (- end start))
