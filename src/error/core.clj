(ns error.core
  (:use [clojure.algo.monads :only [defmonad domonad]]))

(defprotocol IError
  (error? [this]))

(extend-protocol IError
  Object
  (error? [_] false)
  nil
  (error? [_] true)
  Throwable
  (error? [_] true))

(defmonad error-m
  [m-result identity
   m-bind   (fn [m f] (if (error? m)
                        m
                        (f m)))])

(defmacro if-let+
  ([bindings return]
     `(domonad error-m ~bindings ~return))
  ([bindings return else]
     `(let [result# (if-let+ ~bindings ~return)]
        (if (error? result#)
          ~else
          result#))))