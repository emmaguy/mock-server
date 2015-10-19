public class IpParser {
    private String ipAddress;

    public IpParser(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public byte[] toByteArray() {
        return ipToByteArray(ipToLong(ipAddress));
    }

    private static byte[] ipToByteArray(long ipAddress) {
        final byte part1 = (byte) ((ipAddress >> 24) & 0xFF);
        final byte part2 = (byte) ((ipAddress >> 16) & 0xFF);
        final byte part3 = (byte) ((ipAddress >> 8) & 0xFF);
        final byte part4 = (byte) ((ipAddress) & 0xFF);

        return new byte[]{part1, part2, part3, part4};
    }

    private static long ipToLong(final String ipAddress) {
        final String[] ip = ipAddress.split("\\.");

        long num = 0;
        for (int i = 0; i < ip.length; i++) {
            final int power = 3 - i;
            num += ((Integer.parseInt(ip[i]) % 256 * Math.pow(256, power)));

        }

        return num;
    }
}
