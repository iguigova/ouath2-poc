(ns clj-oauth2-poc.core
  (:require [clj-oauth2.client :as oauth2]
            [clojure.java.io :as io]
            [clj-http.client :as http]
            [compojure.core :refer (defroutes GET context)]
            [ring.util.response :as response]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.adapter.jetty :as jetty])
   (:gen-class))

(def redirect-url "http://localhost:8080/callback")
(def grant-type "authorization_code")

(def google-oauth2
  {:authorization-uri "https://accounts.google.com/o/oauth2/auth"
   :access-token-uri "https://oauth2.googleapis.com/token"
   :redirect-uri redirect-url
   :client-id "274509872883-u8qvv148hvdpof4gcbhoi8ouucomvr1d.apps.googleusercontent.com"
   :client-secret "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"
   :access-query-param :access_token
   :scope ["https://www.googleapis.com/auth/userinfo.email"]
   :grant-type grant-type})

(def auth-request
  (oauth2/make-auth-request google-oauth2))

(defn get-access-token [request]
  (let [token (oauth2/get-access-token google-oauth2 (:params request) auth-request)]))

(defn display-token [request]
  (let [token (oauth2/get-access-token google-oauth2 (:params request) auth-request)
        token-info (:body (http/get "https://www.googleapis.com/oauth2/v1/tokeninfo"
                                    {:query-params {:access_token (:access-token token)}
                                     :as :json}))]
                                        ;(response/response {:token token :info token-info})
    {:status 200 :body (str {:token token :info token-info})}))

;; (defn save-token [request]
;;   (let [token (oauth2/get-access-token google-oauth2 (:params request) auth-request)
;;         token-info (:body (http/get "https://www.googleapis.com/oauth2/v1/tokeninfo"
;;                                     {:query-params {:access_token (:access-token token)}
;;                                      :as :json}))]
;;     (with-open [out (io/writer (io/file (:token-directory google-oauth2 "/tmp") (:email token-info)))]
;;       (pprint {:token token
;;                :info token-info}
;;               out))
;;     (response/redirect (:success-url google-oauth2 "/"))))

;; (defn- google-user-email [access-token]
;;   (let [response (oauth2/get "https://www.googleapis.com/oauth2/v1/userinfo" {:oauth access-token})]
;;     (get (parse-string (:body response)) "email")))

(defroutes auth-controller
  (GET "/" [] (response/response "No browsable content on this server"))
  (GET "/google" [] (response/redirect (:uri auth-req)))
  (GET "/callback" [] display-token))

(defn -main [& args]
  (jetty/run-jetty (-> auth-controller
                       wrap-keyword-params
                       wrap-params)
                   {:port 8080}))
