(ns error.core)

(defprotocol IError
  (error? [this]))

(extend-protocol IError
  Object
  (error? [_] false)
  nil
  (error? [_] true)
  Throwable
  (error? [_] true))

(def ^:private nil- (Object.))

(defmacro if-let+
  ([bindings then]
     `(if-let+ [~@bindings] ~then @#'error.core/nil-))
  ([bindings then else]
     (assert (even? (count bindings)))
     (let [[sym init & more] bindings]
       [sym init more bindings]
       (if sym
         `(let [~sym ~init]
            (if (error? ~sym)
              (if (= ~else @#'error.core/nil-)
                ~sym
                ~else)
              (if-let+ [~@more] ~then ~else)))
         then))))