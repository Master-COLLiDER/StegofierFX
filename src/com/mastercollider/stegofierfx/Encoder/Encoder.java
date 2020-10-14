package com.mastercollider.stegofierfx.Encoder;


import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.ImageProcessing.ImageFormat;
import com.mastercollider.stegofierfx.ImageProcessing.ImageType;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.PixelLSBInsertion;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel_ARGB;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel_RGB;
import com.mastercollider.stegofierfx.utils.StringConversionUtility;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.mastercollider.stegofierfx.Config.*;

public class Encoder {

    private int imageWidth;
    private int imageHeight;

    private String inputFileName;
    private BufferedImage coverImage;
    private ImageFormat inputImageFormat;
    private String outputFileName;

    private String Message;

    private EncryptionType encryptionType;
    private ColorChannel colorChannel;
    private int no_of_LSB;

    private int x = 0,y = 0;
    private boolean isARGB = false;

    private Pixel pixel;

    private String Header_bits;
    private String Message_bits;

//    private ArrayList<short> Header_bit_List = new ArrayList<short>();
//    private ArrayList<short> Message_bit_List = new ArrayList<short>();

    public Encoder(@NotNull String inputFileName,@NotNull String outputFileName,@NotNull String message,
                   @NotNull EncryptionType encryptionType, @NotNull ColorChannel colorChannel, int no_of_LSB) throws IOException {

        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.Message = message;
        this.encryptionType = encryptionType;
        this.colorChannel = colorChannel;
        this.no_of_LSB = no_of_LSB;



        inputImageFormat = ImageFormat.doDetect(this.inputFileName);

        if(inputImageFormat == ImageFormat.NOT_DEFINED){
            throw new RuntimeException("Image is not valid format.");
        }
        else
        if (inputImageFormat == ImageFormat.JPEG)
        {
            System.out.println("Input image format is JPEG, it will be converted to PNG format.");
            inputImageFormat = ImageFormat.PNG;
        }


        this.coverImage = ImageIO.read(new File(this.inputFileName));
        this.imageWidth = coverImage.getWidth();
        this.imageHeight = coverImage.getHeight();

        int maxTotalRequiredBytes = HEADER_LENGTH_BYTE+Message.length();


        System.out.println(colorChannel.toString().length());
        System.out.println(no_of_LSB);
        int bitsPerPixel = colorChannel.toString().length()*(no_of_LSB+1);
        int MaxRequiredPixels = (int)(((maxTotalRequiredBytes * 8) /(float)bitsPerPixel)+0.9999f);


        if ((this.imageWidth*this.imageHeight)<MaxRequiredPixels)
        {
            System.out.println(this.imageWidth*this.imageHeight);
            System.out.println(MaxRequiredPixels);
            throw new RuntimeException("The cover image is too small");
        }


        if (coverImage.getType()!= 5 && coverImage.getType()!= 6)
        {
            System.out.println("Converting input Image to "+"BufferedImage.TYPE_4BYTE_ABGR");
            this.coverImage = ImageType.doConvert(this.coverImage,BufferedImage.TYPE_4BYTE_ABGR);
        }


        if (coverImage.getType() == BufferedImage.TYPE_4BYTE_ABGR)
        {
            isARGB = true;
        }

    }


    public void Encode(){
        System.out.println("Encoding Started!");

        prepareHeader();   //prepare Header
        preparePayload();  //prepare Payload

        System.out.println("Encoding Header!");
        //Encoding Header
        getPixel();
        for (int b = 0; b< HEADER_LENGTH_BYTE*8; b++)
        {
            pixel = PixelLSBInsertion.doInsert(pixel,HEADER_COLOR_CHANNEL,Integer.parseInt(""+Header_bits.charAt(b)),HEADER_LSB);
            coverImage.setRGB(x,y,pixel.getValue());
            getNextPixel();
        }

        System.out.println("Encoding Payload!");
        //Encoding Payload


        ColorChannel[] cc = new ColorChannel[colorChannel.toString().length()];
        if (colorChannel.toString().length() == 3)
        {
            cc[0] = ColorChannel.R;
            cc[1] = ColorChannel.G;
            cc[2] = ColorChannel.B;
        }
        else if (colorChannel.toString().length() == 2)
        {
            if (colorChannel == ColorChannel.RG){
                cc[0] = ColorChannel.R;
                cc[1] = ColorChannel.G;
            }else if (colorChannel == ColorChannel.GB){
                cc[0] = ColorChannel.G;
                cc[1] = ColorChannel.B;
            }
            else{
                cc[0] = ColorChannel.R;
                cc[1] = ColorChannel.B;
            }
        }
        else
            cc[0] = colorChannel;


        for (int b = 0; b< Message_bits.length(); b++)
        {

            for (int c = 0; c < cc.length; c++)
            {
                for (int i = 0; i <=no_of_LSB; i++)
                {
                    pixel = PixelLSBInsertion.doInsert(pixel,cc[c],Integer.parseInt(""+Message_bits.charAt(b)),i);
                    coverImage.setRGB(x,y,pixel.getValue());
                    if (i<no_of_LSB)
                        b++;
                    if (b>=Message_bits.length())
                        break;
                }
                if (c<cc.length-1)
                    b++;
                if (b>=Message_bits.length())
                    break;
            }
            if (b<Message_bits.length())
                getNextPixel();
        }
        System.out.println("Encoding Complete!");
    }


    private void getNextPixel()
    {
        x++;
        if(x>=imageWidth)
        {
            y++;
            x=0;
        }
        if(y>=imageHeight)
            throw new RuntimeException("Image Out of Bound");

        getPixel();
    }



    private void prepareHeader(){
        String imageDetectorPatternBits = StringConversionUtility.convertStringToBinary(STEGO_IMAGE_DETECTOR_PATTERN);
        String colorChannelBinary = StringConversionUtility.
                intToBinaryLength(ColorChannel.getColorChannelToInt(colorChannel), COLOR_CHANNEL_STORE*8);
        String encodeTypeBinary = StringConversionUtility.
                intToBinaryLength(EncryptionType.getEncryptionType(encryptionType),ENCRYPTION_TYPE_STORE*8);
        String noOfLSBBinary = StringConversionUtility.
                intToBinaryLength(no_of_LSB,NO_OF_LSB_STORE*8);
        String messageLengthBinary = StringConversionUtility.
                intToBinaryLength(Message.length(),MESSAGE_LENGTH_STORE*8);

        Header_bits = imageDetectorPatternBits + colorChannelBinary +encodeTypeBinary +noOfLSBBinary +messageLengthBinary;
    }

    private void preparePayload(){
        Message_bits = StringConversionUtility.convertStringToBinary(Message);
    }


    private void getPixel()
    {
        if (isARGB)
            pixel = new Pixel_ARGB(x,y,coverImage.getRGB(x,y));
        else
            pixel = new Pixel_RGB(x,y,coverImage.getRGB(x,y));
    }


    public void saveImage() throws IOException {
        String outputFileExtension = inputImageFormat.toString();
        outputFileName = outputFileName.substring(0,outputFileName.lastIndexOf(".")+1)+outputFileExtension;
        File outputFile = new File(outputFileName);
        if(!ImageIO.write(coverImage, outputFileExtension, outputFile))
        {
            throw new IOException("Encoder Writing Failed");
        }
        System.out.println("Saving Complete!");
    }
}
