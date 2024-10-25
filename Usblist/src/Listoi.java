package lili;




import org.usb4java.*;

public class Listoi{
    public static void main(String[] args) {
        // Initialize the USB subsystem
        Context context = new Context();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize libusb.", result);
        }

        // Get the list of USB devices
        DeviceList list = new DeviceList();
        
        result = LibUsb.getDeviceList(context, list);
        int cd=list.getSize();
        if (result < 0) {
            throw new LibUsbException("Unable to get device list.", result);
        }

        // Iterate over the devices
        try {
            for (int i = 0; i < cd; i++) {
                Device device = list.get(i);
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result < 0) {
                    throw new LibUsbException("Unable to read device descriptor.", result);
                }

                // Print device information
                System.out.print("Device---:"+i+"\n");
                System.out.printf("Device---: %04x:%04x%n", descriptor.idVendor(), descriptor.idProduct(),"lll");
            }
        } finally {
            // Free the device list
            LibUsb.freeDeviceList(list, true);
        }

        // Deinitialize the USB subsystem
        LibUsb.exit(context);
    }}
