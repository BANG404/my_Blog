package com.ibat.myblog.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class CurlRequest {

    public static void main(String[] args) {
        String url = "https://r.jina.ai/https://y.qq.com/n/ryqq/songDetail/004d8TPg3Sn98k";
        String cookie = "RK=TwdUKkgmEA; ptcz=f232012b79ddc41daab3af1e13d1ca5dff118379b5eda17752cb990b618c56a3; pgv_pvid=1267861608; fqm_pvqid=f2ccfb04-e097-4b4c-b212-7603833c8e3d; fqm_sessionid=54c0971b-4d3d-4afa-90da-da0f2ffa7725; pgv_info=ssid=s4908299877; ts_uid=7089702842; login_type=1; euin=oKok7KvAoe-Foc**; tmeLoginType=2; psrf_musickey_createtime=1734693809; psrf_access_token_expiresAt=1735298609; qm_keyst=Q_H_L_63k3Nd92HCjOmA0is1EsjB66tHf3NPv5tA3OujhEP335xjNFBToWWn0UMHE3t817t9Qwu3n_bpKsNceO_D7cfQA; qqmusic_key=Q_H_L_63k3Nd92HCjOmA0is1EsjB66tHf3NPv5tA3OujhEP335xjNFBToWWn0UMHE3t817t9Qwu3n_bpKsNceO_D7cfQA";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Return-Format", "html");
            con.setRequestProperty("X-Set-Cookie", cookie);

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
