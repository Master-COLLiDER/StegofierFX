package com.mastercollider.stegofierfx.Decoder;

import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.ImageProcessing.ImageFormat;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.PixelLSBExtraction;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel_ARGB;
import com.mastercollider.stegofierfx.ImageProcessing.pixel.Pixel_RGB;
import com.mastercollider.stegofierfx.utils.StringConversionUtility;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.mastercollider.stegofierfx.Config.*;

public class Decoder {

    private int imageWidth;
    private int imageHeight;

    private String inputFileName;
    private BufferedImage coverImage;
    private ImageFormat inputImageFormat;

    private String Message;

    private EncryptionType encryptionType;
    private ColorChannel colorChannel;
    private int no_of_LSB;

    private int MessageLength;
    private int x = 0,y = 0;
    private boolean isARGB = false;

    private Pixel pixel;

    private String Header_bits="";
    private String Message_bits="";


    public Decoder(@NotNull String inputFileName) throws IOException {
        this.inputFileName = inputFileName;

        inputImageFormat = ImageFormat.doDetect(this.inputFileName);

        if(inputImageFormat == ImageFormat.NOT_DEFINED|| inputImageFormat == ImageFormat.JPEG){
            throw new RuntimeException("Image is not valid format.");
        }

        this.coverImage = ImageIO.read(new File(this.inputFileName));
        this.imageWidth = coverImage.getWidth();
        this.imageHeight = coverImage.getHeight();


    }

    private void stegoChecker()
    {
        if ((this.imageWidth*this.imageHeight)<HEADER_LENGTH_BYTE*8)
        {
            throw new RuntimeException("Not a stego image!");
        }
        else
        if (coverImage.getType()!= 5 && coverImage.getType()!= 6)
        {
            throw  new RuntimeException("Invalid Stego Image type!");
        }

        if (!isStegoImage())
            throw new RuntimeException("Not a stego image!");
    }



    public void DecodeHeader(){
        //decoding header
        System.out.println("Decoding Header!");
        for (int b = 0; b< (HEADER_LENGTH_BYTE-STEGO_IMAGE_DETECTOR_PATTERN_STORE)*8; b++)
        {
            Header_bits = Header_bits+PixelLSBExtraction.doExtract(pixel,HEADER_COLOR_CHANNEL,HEADER_LSB);
            getNextPixel();
        }

        this.colorChannel = ColorChannel.getColorChannel(StringConversionUtility.binaryToInt(Header_bits.substring(0,COLOR_CHANNEL_STORE*8)));
        this.encryptionType = EncryptionType.getEncryptionType(StringConversionUtility.binaryToInt(
                Header_bits.substring(COLOR_CHANNEL_STORE*8,
                        COLOR_CHANNEL_STORE*8+ENCRYPTION_TYPE_STORE*8)));
        this.no_of_LSB = StringConversionUtility.binaryToInt(Header_bits.substring(+COLOR_CHANNEL_STORE*8+ENCRYPTION_TYPE_STORE*8,
                COLOR_CHANNEL_STORE*8+ENCRYPTION_TYPE_STORE*8+NO_OF_LSB_STORE*8));
        this.MessageLength =  StringConversionUtility.binaryToInt(Header_bits.substring(
                COLOR_CHANNEL_STORE*8+ENCRYPTION_TYPE_STORE*8+NO_OF_LSB_STORE*8));

        System.out.println("Header Decoding Completed!");

        System.out.println(this.colorChannel);
        System.out.println(this.encryptionType);
        System.out.println(this.no_of_LSB);
        System.out.println(this.MessageLength);
    }

    public void DecodeBody(){
        //decoding payload
        System.out.println("Decoding  Body!");
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
        for (int b = 0; b< MessageLength*8; b++)
        {
            for (int c = 0; c < cc.length; c++)
            {
                for (int i = 0; i <=no_of_LSB; i++)
                {
                    Message_bits = Message_bits+PixelLSBExtraction.doExtract(pixel,cc[c],i);

                    if (i<no_of_LSB)
                        b++;
                    if (Message_bits.length()>=MessageLength*8)
                        break;
                }
                if (c<cc.length-1)
                    b++;
                if (Message_bits.length()>=MessageLength*8)
                    break;
            }
            if (Message_bits.length()<MessageLength*8)
                getNextPixel();
        }
        this.Message = StringConversionUtility.convertBinaryToString(Message_bits);
    }
    public void Decode()
    {
        System.out.println("Checking Stego!");
        stegoChecker();
        System.out.println("Decoding Started!");
        DecodeHeader();
        DecodeBody();
        System.out.println("Decoding Completed!");
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

    private void getPixel()
    {
        if (isARGB)
            pixel = new Pixel_ARGB(x,y,coverImage.getRGB(x,y));
        else
            pixel = new Pixel_RGB(x,y,coverImage.getRGB(x,y));
    }



    public boolean isStegoImage()
    {
        x = 0; y = 0;

        if (coverImage.getType() == BufferedImage.TYPE_4BYTE_ABGR)
        {
            isARGB = true;
        }

        String Detection_Bits="";
        System.out.println("Detection Started!");
        getPixel();
        for (int b = 0; b< STEGO_IMAGE_DETECTOR_PATTERN_STORE*8; b++)
        {
            Detection_Bits = Detection_Bits+PixelLSBExtraction.doExtract(pixel,HEADER_COLOR_CHANNEL,HEADER_LSB);
            getNextPixel();
        }

        String DetectionPattern = StringConversionUtility.convertBinaryToString(Detection_Bits);

        if (DetectionPattern.matches(STEGO_IMAGE_DETECTOR_PATTERN))
            System.out.println("Stego Image Detected!");
        return DetectionPattern.matches(STEGO_IMAGE_DETECTOR_PATTERN);
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public String getMessage() {
        return Message;
    }

    public ColorChannel getColorChannel() {
        return colorChannel;
    }

    public int getNo_of_LSB() {
        return no_of_LSB;
    }
}
