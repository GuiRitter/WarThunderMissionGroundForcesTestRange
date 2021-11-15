# Gound Attack/Forces Test Range

A War Thunder User Mission where you can check out every single player controllable tank. This is a project to help generate this mission whenever there's an update.

1. Download [SysExporter](https://www.nirsoft.net/utils/sysexp.html).
2. Download latest [CDK](http://wiki.warthunder.com/index.php?title=Download_War_Thunder_CDK).
3. Run *CDK* (`missioned.cmd`).
4. Place new `tankModels`.
5. Open *Object Properties* tab to load *Class* combo box in memory.
6. Run `list/getList.bat` passing the *CDK*'s *PID* as a parameter and wait for it to finish.
7. Sort *ComboBox* list by *Items* and click on the biggest one.
8. Right click on the bottom list and choose *Export All Items*.
9. Save as `list %s.txt`, where `%s` is the current game version, formatted to only include the major version number (for example, `1.23`).
10. Close the *CDK* and *SysExporter*.
11. Run `Java/WarThunderListDiffer`, open the list file for the penultimate version, then the list file for the current version, and save as `list diff %s %s.txt`, where the first `%s` is the penultimate version and the second `%s` is the current version.
12. Open the most recent `list diff %s %s.txt`.
13. Access [Ground Vehicles - War Thunder Wiki](https://wiki.warthunder.com/Ground_vehicles).
14. For every nation
    1. Run `Java/WarThunderTreeTableManager`.
    2. Open `data/%s.json` with it, where `%s` is the nation.
    3. Update the table with the new entries from `list diff %s %s.txt`.
    4. Overwrite `data/%s.json`.
15. Close *War Thunder*.
16. Run `Java/WarThunderGroundAttackTestRangeGenerator` and choose the `data` folder.
17. Copy the generated *BLK*s with names ending in `_screenshot` to *War Thunder*'s `UserMissions` folder.
18. Run *War Thunder* with 1280×720 resolution, windowed or not.
19. For every mission that ends in `_screenshot.blk`.
    1. Play the mission.
    2. Take the screenshot with *F12*.
    3. Check that the tanks are all present in the image without being clipped and that the empty space at the borders are minimal.
        1. If not, use `screenshot.txt` as a guide to adjust the camera.
        2. Adjust the spread first.
            1. Measure the length of the empty space on the left and on the right. Pick the smallest one.
            2. Measure the length of the empty space on the top and on the bottom. Take the average.
            3. Adjust the spread until both measures are close.
        3. Adjust the camera height second.
            1. Measure the length of the empty space on the top and on the bottom.
            2. Adjust the camera height until both measures are close.
        4. Adjust the camera zoom until the empty spaces on the borders are small enough.
        5. This will impact on the spread and camera height, so more rounds might be needed.
20. Run *War Thunder* with *Custom* graphic settings, all maxed, and 1280×720 resolution.
21. For every mission that ends in `_screenshot.blk`.
    1. Play the mission.
    2. Wait for textures to load completely and for some animations to run.
    3. Take the screenshot with *F12*.
    4. Use the `hires` image if the website allows the file size.
22. Zip everything and publish it.
