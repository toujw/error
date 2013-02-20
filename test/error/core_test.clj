(ns error.core-test
  (:use clojure.test
        error.core))

(defrecord TestSuccess []
  IError
  (error? [_] false))

(defrecord TestError []
  IError
  (error? [_] true))

(defn- safety []
  (throw (Exception. "This shouldn't happen!")))

(deftest if-let+-test
  (testing "Success"
    (is (= (if-let+ [] 1) 1))
    (is (= (if-let+ [] 1 2) 1))
    (is (= (if-let+ [a 1] a) 1))
    (is (= (if-let+ [a 1] a 2) 1))
    (is (= (if-let+ [a 1 b 2] a) 1))
    (is (= (if-let+ [a 1 b 2] a 2) 1))
    (is (= (if-let+ [a (TestSuccess.)] 1) 1))
    (is (= (if-let+ [a (TestSuccess.)] 1 2) 1))
    (is (= (if-let+ [_ true] 1) 1))
    (is (= (if-let+ [a 1] nil) nil)))
  (testing "Failure"
    (is (= (if-let+ [a nil b (safety)] 1) nil))
    (is (= (if-let+ [a nil b (safety)] 1 2) 2))
    (is (= (let [t (Throwable.)] (if-let+ [a t b (safety)] 1) t)))
    (is (= (let [t (Throwable.)] (if-let+ [a t b (safety)] 1 2) 2)))
    (is (= (let [t (TestError.)] (if-let+ [a t b (safety)] 1) t)))
    (is (= (let [t (TestError.)] (if-let+ [a t b (safety)] 1 2) 2)))
    (is (= (let [t (Throwable.)] (if-let+ [_ t b (safety)] 1) t)))
    (is (= (let [t (Throwable.)] (if-let+ [_ t b (safety)] 1 2) 2)))
    (is (= (if-let+ [_ nil b (safety)] 1) nil))
    (is (= (if-let+ [_ nil b (safety)] 1 2) 2))
    (is (= (if-let+ [a (Throwable.) b (safety)] nil nil) nil))))