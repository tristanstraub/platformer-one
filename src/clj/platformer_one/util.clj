(ns platformer-one.util)


(defmacro print-once [& arg]
  `(do (defonce printed# (atom nil))
       (when (not @printed#)
         (print ~@arg)
         (reset! printed# true))))
