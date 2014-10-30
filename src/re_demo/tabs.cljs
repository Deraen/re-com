(ns re-demo.tabs
   (:require [reagent.core :as reagent]
             [alandipert.storage-atom :refer [local-storage]]
             [re-com.box   :refer [h-box v-box box gap line scroller border]]
             [re-com.dropdown :refer [single-dropdown]]
             [re-com.core  :refer [button label title]]
             [re-com.tabs  :as tabs]))


(def demos [{:id 1 :label "Horizontal Tabs"}
            {:id 2 :label "Persistent Tab Selection"}
            {:id 3 :label "Dynamic Tabs"}
            {:id 4 :label "Vertical Tabs"}])


;; Define some tabs.
;; A tab definition need only consist of an :id and a :label. The rest is up to you
;; Below, you'll note that all ids are namespaced keywords, but they can be anything.
;;
(def tabs-definition
  [ {:id ::tab1  :label "Tab1"   :say-this  "I don't like my tab siblings, they smell."}
    {:id ::tab2  :label "Tab2"   :say-this  "Don't listen to Tab1, he's just jealous of my train set."}
    {:id ::tab3  :label "Tab3"   :say-this  "I'm telling Mum on you two !!"}])


(defn horizontal-tabs-demo
  []
    (let [selected-tab-id (reagent/atom (:id (first tabs-definition)))]   ;; holds the id of the selected tab
    (fn []
      [v-box
       :children [[h-box
                   :gap "50px"
                   :min-width "1200px"
                   :children [[v-box
                               :size "100%"
                               :gap     "30px"
                               :margin  "20px 0px 0px 0px"        ;; TODO:  decide would we prefer to use :top-margin??
                               :children [
                                           ;; Three visual variations on tabs follow
                                           [tabs/horizontal-pills
                                            :model selected-tab-id
                                            :tabs  tabs-definition]

                                           [tabs/horizontal-bar-tabs
                                            :model selected-tab-id
                                            :tabs  tabs-definition]

                                           [tabs/horizontal-tabs
                                            :model selected-tab-id
                                            :tabs  tabs-definition]

                                           ;; Display the tab content which, in this case, is a string
                                           ;; extracted from the tab definition.
                                           ;; We out a dotted border around it for dramatic effect.
                                           [border
                                            :border  "1px dashed grey"
                                            :radius  "10px"
                                            :padding "20px"
                                            ;;:margin  "4px"
                                            ;:size    "50%"
                                            :child [:p (:say-this (tabs/find-tab @selected-tab-id tabs-definition))]]]]]]]])))


(defn remembers-demo
  []
  (let [tab-defs        [{:id ::1 :label "1"}
                         {:id ::2 :label "2"}
                         {:id ::3 :label "3" }]
        id-store        (local-storage (atom nil) ::id-store)
        selected-tab-id (reagent/atom (if  (nil? @id-store) (:id (first tab-defs)) @id-store))   ;; id of the selected tab
        _               (add-watch selected-tab-id nil #(reset! id-store %4))]                   ;; put into local-store for later
    (fn []
      [v-box
       :children [[h-box
                   :gap "50px"
                   :min-width "1000px"
                   :children [[v-box
                               :size "50%"
                               :gap     "30px"
                               :margin  "20px 0px 0px 0px"       ;; TODO:  decide would we prefer to use :top-margin??
                               :children [[tabs/horizontal-tabs
                                           :model  selected-tab-id
                                           :tabs  tab-defs]]]]]]])))


(defn adding-tabs-demo
  []
  (let [tab-defs        (reagent/atom [{:id ::1 :label "1"}
                                       {:id ::2 :label "2"}
                                       {:id ::3 :label "3"}])
        selected-tab-id (reagent/atom (:id (first @tab-defs)))]
    (fn []
      [v-box
       :children [[h-box
                   :gap "50px"
                   :children [[v-box
                               :size    "auto"
                               :margin  "20px 0px 0px 0px"        ;; TODO:  i supplied "start" (string) instead of :start and got runtime errors ... better protection
                               :align   :start
                               :children [[button
                                           :label "Add"
                                           :on-click (fn []
                                                       (let [c       (str (inc (count @tab-defs)))
                                                             new-tab {:id (keyword c) :label c}]
                                                         (swap! tab-defs conj new-tab)))]]]
                              [v-box
                               :size "50%"
                               :gap     "30px"
                               :margin  "20px 0px 0px 0px"       ;; TODO:  decide would we prefer to use :top-margin??
                               :children [[tabs/horizontal-tabs
                                           :model  selected-tab-id
                                           :tabs  tab-defs]]]]]]])))

(defn vertical-tabs-demo
  []
  (let [selected-tab-id (reagent/atom (:id (first tabs-definition)))]     ;; this atom holds the id of the selected
    (fn []
      [:div
       "TBA"])))

(defn notes
  [selected-demo-id]
  [v-box
   :width    "500px"
   :style    {:font-size "small"}
   :children [[:div.h4 "General notes"]
              [:ul
               [:li "To create a dropdown component, the following parameters are required:"
                [:ul
                 [:li.spacer [:code ":choices"] " - a vector of maps. Each map contains a unique :id and a :label and can optionally include a :group."]
                 [:li.spacer [:code ":model"] " - the :id of the initially selected choice, or nil to have no initial selection (in which case, :placeholder will be shown)."]
                 [:li.spacer [:code ":on-change"] " - a callback function taking one parameter which will be the :id of the new selection."]]]]
              (case @selected-demo-id
                1 [:div
                   [:div.h4 "Horizontal tabs notes:"]
                   [:ul
                    [:li "Tab-like controls can be styled in the 3 ways shown to the right."]
                    [:li "All 3 share the same state so they change in lockstep."]
                    [:li "Placeholder  \"Tab Contents\" (a string of text) is shown in the dotted border below. Just for effect."]
                    [:li "The implementation here is simple. As a result, your selection is forgotten when you change to another panel, like Welcome (look top left)."]
                    [:li "The code for this page can be found in /src/re_demo/tabs.cljs"]
                    [:li "For another demonstration, also look in /src/re_demo/core.cljs. After all, this entire demo app is just a series of tabs."]]]
                2 [:div
                   [:div.h4 "Peristent tabs notes:"]
                   [:ul
                    [:li "Any tab selection you make on the right will persist."]
                    [:li "It is stored using HTML5's local-storage."]
                    [:li "Even if you refresh the entire browser page, you'll see the same selection."]]]
                3 [:div
                   [:div.h4 "Dynamic tabs notes:"]
                   [:ul
                    [:li "Click  \"Add\" for more tabs."]]]
                4 [:div
                   [:div.h4 "Vertical tabs notes:"]
                   [:ul
                    [:li "TBA"]]])]])


(defn panel
  []
  (let [selected-demo-id (reagent/atom 1)]
    (fn []
      [v-box
       :children [[title "Tabs"]
                  [h-box
                   :gap      "50px"
                   :children [[notes selected-demo-id]
                              [v-box
                               :gap       "15px"
                               :size      "auto"
                               :min-width "500px"
                               :margin    "20px 0px 0px 0px"
                               :children  [[h-box
                                            :gap      "10px"
                                            :align    :center
                                            :children [[label :label "Select a demo"]
                                                       [single-dropdown
                                                        :choices   demos
                                                        :model     selected-demo-id
                                                        :width     "300px"
                                                        :on-change #(reset! selected-demo-id %)]]]
                                           [gap :size "0px"] ;; Force a bit more space here
                                           (case @selected-demo-id
                                             1 [horizontal-tabs-demo]
                                             2 [remembers-demo]
                                             3 [adding-tabs-demo]
                                             4 [vertical-tabs-demo])]]]]]])))
