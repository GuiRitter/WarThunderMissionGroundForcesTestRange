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
12. Run `Java/crawl_wiki.bat` and check if the new additions match the diff file generated previously.
    1. If they don't, the solution depends on a case by case analysis.
13. Run `Java/WarThunderWikiTreeBridger` to see what was added, removed or moved.
14. Update the `data/%s.json` files manually, where `%s` is the nation, to include the changes from the `data/bridge/%s.json` files.
    1. You can run a diff between these two to make it easier. However, be aware that there will be some vehicles that are exclusive to the `data/%s.json` files because they are not in the wiki. These have been added according to case by case analyses and should not follow the `data/bridge/%s.json` files.
    2. For the vehicles's names, look in the game's tech tree. If the vehicle is not there, run the next steps until you run a mission and find these vehicles there. Then, use the name that appears in game.
15. Run `Java/WarThunderWikiTreeBridger` again to add the names found previously.
16. Run `Java/generator.bat`.
17. Run *War Thunder* with 1280×720 resolution, windowed or not.
18. For every mission that ends in `_screenshot.blk`.
    1. Play the mission.
    2. Take the screenshot with *F12*.
    3. Check that the tanks are all present in the image without being clipped and that the empty space at the borders are minimal.
        1. If not, use `screenshot.txt` as a guide to adjust the camera (values for `area_for_cutscene_0` and `area_for_cutscene_1` in `%s footer.txt`).
        2. Adjust the spread first.
            1. Measure the length of the empty space on the left and on the right. Pick the smallest one.
            2. Measure the length of the empty space on the top and on the bottom. Take the average.
            3. Adjust the spread until both measures are close.
        3. Adjust the camera height second.
            1. Measure the length of the empty space on the top and on the bottom.
            2. Adjust the camera height until both measures are close.
        4. Adjust the camera zoom until the empty spaces on the borders are small enough.
        5. This will impact on the spread and camera height, so more rounds might be needed.
19. Run *War Thunder* with *Custom* graphic settings, all maxed, and 1280×720 resolution.
20. For every mission that ends in `_screenshot.blk`.
    1. Play the mission.
    2. Wait for textures to load completely and for some animations to run.
    3. Take the screenshot with *F12*.
    4. Use the `hires` image if the website allows the file size.
21. Zip everything and publish it.
