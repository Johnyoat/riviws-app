package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipromarketing.riviws.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicy extends DialogFragment {

    private String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset='utf-8'/>\n" +
            "  <meta name='viewport' content='width=device-width'/>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "  <h2>Privacy Policy</h2>\n" +
            "  <p> Hipro Marketing built the Riviws app as a Free app. This SERVICE is provided by\n" +
            "    Hipro Marketing at no cost and is intended for use as is.\n" +
            "  </p>\n" +
            "  <p>This page is used to inform visitors regarding our policies with the collection, use, and disclosure\n" +
            "    of Personal Information if anyone decided to use our Service.\n" +
            "  </p>\n" +
            "  <p>If you choose to use our Service, then you agree to the collection and use of information in\n" +
            "    relation to this policy. The Personal Information that we collect is used for providing and improving\n" +
            "    the Service. We will not use or share your information with anyone except as described\n" +
            "    in this Privacy Policy.\n" +
            "  </p>\n" +
            "  <p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is\n" +
            "    accessible at Riviws unless otherwise defined in this Privacy Policy.\n" +
            "  </p>\n" +
            "  <br><p><strong>Information Collection and Use</strong></p>\n" +
            "  <p>For a better experience, while using our Service, we may require you to provide us with certain\n" +
            "    personally identifiable information, including but not limited to Name,email,phone,user image. The information that\n" +
            "    we request will be retained by us and used as described in this privacy policy.\n" +
            "  </p>\n" +
            "  <p>The app does use third party services that may collect information used to identify you.</p>\n" +
            "  <div>\n" +
            "    <p>Link to privacy policy of third party service providers used by the app</p>\n" +
            "    <ul>\n" +
            "      <li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\">Google Play Services</a></li>\n" +
            "      <li><a href=\"https://support.google.com/admob/answer/6128543?hl=en\" target=\"_blank\">AdMob</a></li>\n" +
            "      <li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\">Firebase Analytics</a></li>\n" +
            "      <li><a href=\"https://www.facebook.com/about/privacy\" target=\"_blank\">Facebook</a></li>\n" +
            "      <li><a href=\"https://fabric.io/privacy\" target=\"_blank\">Fabric</a></li>\n" +
            "      <li><a href=\"http://try.crashlytics.com/terms/privacy-policy.pdf\" target=\"_blank\">Crashlytics</a></li>\n" +
            "      <!---->\n" +
            "      <!---->\n" +
            "    </ul>\n" +
            "  </div>\n" +
            "  <br><p><strong>Log Data</strong></p>\n" +
            "  <p> We want to inform you that whenever you use our Service, in a case of\n" +
            "    an error in the app we collect data and information (through third party products) on your phone\n" +
            "    called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address,\n" +
            "    device name, operating system version, the configuration of the app when utilizing our Service,\n" +
            "    the time and date of your use of the Service, and other statistics.\n" +
            "  </p>\n" +
            "  <br><p><strong>Cookies</strong></p>\n" +
            "  <p>Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers.\n" +
            "    These are sent to your browser from the websites that you visit and are stored on your device's internal\n" +
            "    memory.\n" +
            "  </p>\n" +
            "  <p>This Service does not use these “cookies” explicitly. However, the app may use third party code and\n" +
            "    libraries that use “cookies” to collect information and improve their services. You have the option to\n" +
            "    either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose\n" +
            "    to refuse our cookies, you may not be able to use some portions of this Service.\n" +
            "  </p>\n" +
            "  <br><p><strong>Service Providers</strong></p>\n" +
            "  <p> We may employ third-party companies and individuals due to the following reasons:</p>\n" +
            "  <ul>\n" +
            "    <li>To facilitate our Service;</li>\n" +
            "    <li>To provide the Service on our behalf;</li>\n" +
            "    <li>To perform Service-related services; or</li>\n" +
            "    <li>To assist us in analyzing how our Service is used.</li>\n" +
            "  </ul>\n" +
            "  <p> We want to inform users of this Service that these third parties have access to\n" +
            "    your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However,\n" +
            "    they are obligated not to disclose or use the information for any other purpose.\n" +
            "  </p>\n" +
            "  <br><p><strong>Security</strong></p>\n" +
            "  <p> We value your trust in providing us your Personal Information, thus we are striving\n" +
            "    to use commercially acceptable means of protecting it. But remember that no method of transmission over\n" +
            "    the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee\n" +
            "    its absolute security.\n" +
            "  </p>\n" +
            "  <br><p><strong>Links to Other Sites</strong></p>\n" +
            "  <p>This Service may contain links to other sites. If you click on a third-party link, you will be directed\n" +
            "    to that site. Note that these external sites are not operated by us. Therefore, we strongly\n" +
            "    advise you to review the Privacy Policy of these websites. We have no control over\n" +
            "    and assume no responsibility for the content, privacy policies, or practices of any third-party sites\n" +
            "    or services.\n" +
            "  </p>\n" +
            "  <br><p><strong>Children’s Privacy</strong></p>\n" +
            "  <p>These Services do not address anyone under the age of 13. We do not knowingly collect\n" +
            "    personally identifiable information from children under 13. In the case we discover that a child\n" +
            "    under 13 has provided us with personal information, we immediately delete this from\n" +
            "    our servers. If you are a parent or guardian and you are aware that your child has provided us with personal\n" +
            "    information, please contact us so that we will be able to do necessary actions.\n" +
            "  </p>\n" +
            "  <br><p><strong>Changes to This Privacy Policy</strong></p>\n" +
            "  <p> We may update our Privacy Policy from time to time. Thus, you are advised to review\n" +
            "    this page periodically for any changes. We will notify you of any changes by posting\n" +
            "    the new Privacy Policy on this page. These changes are effective immediately after they are posted on\n" +
            "    this page.\n" +
            "  </p>\n" +
            "  <br><p><strong>Contact Us</strong></p>\n" +
            "  <p>If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact\n" +
            "    us.</p>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    public static PrivacyPolicy newInstance() {
        
        Bundle args = new Bundle();
        
        PrivacyPolicy fragment = new PrivacyPolicy();
        fragment.setArguments(args);
        return fragment;
    }
    
    public PrivacyPolicy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.privacy_policy, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatTextView textView = view.findViewById(R.id.privacyPolicy);
        AppCompatImageView cls = view.findViewById(R.id.back);

        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });


       if (isAdded()){
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
           } else {
               textView.setText(Html.fromHtml(html));
           }
       }
    }
}
