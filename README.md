# fs2020-livery-manager
Livery manager for Microsoft's 2020 flight simulator.
Created to lessen the workload for adding liveries to Clink123's megapack

# Currently only works for the A320 Neo!!!!

# Important!
This program doesn't _magically_ add the texture files to the megapack, this you still have to do yourself.
Next to this, because of how the livery system currently works, it is very important that you make sure you put in the right values after running the script.
If you run into problems please look below in the known issues list to see if your problem is there.

## Credits
Credits to @Clink123 on discord for making the original megapack.
Also credits to all the amazing livery makers which make stunning liveries.

# Installation
## Installation with existing megapack
1. Download the .jar and .bat files from the releases section, https://github.com/Lolkilee/fs2020-livery-manager/releases.
2. Move the both of the files into the main folder of the megapack (within the community folder).
3. To use the script use the livery_launch.bat file to start the command line.

## Installation with empty pack
1. Download the .zip file from the releases section, https://github.com/Lolkilee/fs2020-livery-manager/releases.
2. Install the folder "liveries-A20N" (located within the zip file) to your community folder, which is located where you installed your MSFS2020 content.
3. To use the script use the livery_launch.bat file to start the command line.

# Adding liveries
1. Download the livery files provided by the author of the livery example: https://drive.google.com/drive/folders/1wOKaMmCMcWFI2CQLK6FXKOQYC_i8H6S3 (@Miggle, MSFS discord) Most liveries can be found in the #liveries-releases section of the official microsoft flight simulator discord

2. Important that you do this right: after having downloaded the livery, you DO NOT want to copy the whole thing into the discord, this will override the already existing aircraft.cfg file and therefore break every other livery you have in the pack. You'll want to get the TEXTURE.XXX folder from the download and move it to <megapack location>/SimObjects/Airplanes/Asobo_A320_NEO. In this folder you should see the aircraft.cfg file and possibly other TEXTURE.XXX folders. After having done this, make sure to remember what the XXX where in your TEXTURE folder. Example if you were copying TEXTURE.HKE from the link above, you would have to remember the HKE part
  
3. Use the script to add the texture files to the config files. The script will guide you through the process, it is very important that the texture folder name matches up with the folder you want to add. when inputting this, you should only put in the part after the dot in the foldername (e.g. TEXTURE.HKE should be HKE). 

4. If you didn't encounter any errors during the script and you put in the right values at the texture folder input, the livery should be added to the files and will therefore be selectable in game.

# Known issues
## No thumbnails in livery selection
This can be either two things: if the selection bar is bigger than the original thumbnail this means that the game hasn't fully loaded the files yet and it will be available later.
However if that is not the case, most likely the layout.json file within the megapack is faulty and should be fixed see the Textures not showing up issue for more information.
## Textures not showing up
This is cause by a disrepancy in the layout.json file, which disables the game from using the liveries. To fix this it is recommended to try and find someone who has experience in JSON to try and find the error in the file, often this can be a comma at the end of the content array which passes an empty object to the game.
