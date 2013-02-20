(ns error.core-test
  (:use clojure.test
        error.core))

(defrecord TestSuccess []
  IError
  (error? [_] false))

(defrecord TestError []
  IError
  (error? [_] true))

(deftest if-let+-test
  (testing "Success"
    (is (= (if-let+ [a 4] 4) 4))
    (is (= (if-let+ [a 5 b 3] (+ a b)) 8))
    (is (= (if-let+ [a "asdf"] "asdf") "asdf"))
    (is (= (if-let+ [a (TestSuccess.)] (TestSuccess.)) (TestSuccess.))))
  (testing "Failure"
    (is (= (if-let+ [a nil] true) nil))
    (is (= (let [t (Throwable.)] (if-let+ [a t] true) t)))
    (is (= (if-let+ [a (TestError.)] true) (TestError.))))
  (testing "Then"
    (is (= (if-let+ [a nil b 5] true false) false))
    (is (= (if-let+ [a true b 5] true false) true))))