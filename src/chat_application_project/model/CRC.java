package chat_application_project.model;

public class CRC {
    static String Xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        int n = b.length();
        for (int i = 1; i < n; i++) {
            if (a.charAt(i) == b.charAt(i))
                result.append("0");
            else
                result.append("1");
        }
        return result.toString();
    }

    static String mod2thingy(String dividend, String divisor) {
        int pick = divisor.length();
        String tmp = dividend.substring(0, pick);

        int n = dividend.length();

        while (pick < n) {
            if (tmp.charAt(0) == '1')
                tmp = Xor(divisor, tmp)
                        + dividend.charAt(pick);
            else
                tmp = Xor(new String(new char[pick])
                                .replace("\0", "0"),
                        tmp)
                        + dividend.charAt(pick);
            pick += 1;
        }

        if (tmp.charAt(0) == '1')
            tmp = Xor(divisor, tmp);
        else
            tmp = Xor(new String(new char[pick])
                            .replace("\0", "0"),
                    tmp);

        return tmp;
    }

    static void encoded(String data, String key) {
        int l_key = key.length();
        String appended_data
                = (data
                + new String(new char[l_key - 1])
                .replace("\0", "0"));
        String remainder = mod2thingy(appended_data, key);
        String codeword = data + remainder;
        //System.out.println("Remainder : " + remainder);
        System.out.println(
                "Data :" + codeword
                        + "\n");
    }

    static void Receiver(String data, String key) {
        String cxor
                = mod2thingy(data.substring(0, key.length()), key);
        int curr = key.length();
        while (curr != data.length()) {
            if (cxor.length() != key.length()) {
                cxor += data.charAt(curr++);
            } else {
                cxor = mod2thingy(cxor, key);
            }
        }
        if (cxor.length() == key.length()) {
            cxor = mod2thingy(cxor, key);
        }
        if (cxor.contains("1")) {
            System.out.println(
                    "some error in data");
        } else {
            System.out.println("correct message received");
        }
    }

    // Driver code
    public void initializer(String data) {
        String key = "1010";
        encoded(data, key);

        System.out.println("Receiver: ");
        Receiver(data + mod2thingy(data + new String(new char[key.length() - 1])
                .replace("\0", "0"), key), key);
    }
}


