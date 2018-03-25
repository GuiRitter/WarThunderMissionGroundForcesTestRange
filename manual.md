0. Download latest [CDK](http://wiki.warthunder.com/index.php?title=Download_War_Thunder_CDK).
1. Run *CDK* (`missioned.cmd`).
2. Place new `tankModels`.
3. Open *Object Properties* tab to load *Class* combo box in memory.
4. Run `list/getList.bat` passing the *CDK*'s *PID* as a parameter and wait for it to finish.
5. Sort *ComboBox* list by *Items* and click on the biggest one.
6. Right click on the bottom list and choose *Export All Items*.
7. Save as `list %s.txt`, where `%s` is the current game version, formatted to only include the major version number (for example, `1.23`).
8. Close the *CDK* and *SysExporter*.
9. Run `WarThunderListDiffer`, open the list file for the penultimate version, then the list file for the current version, and save as `list diff %s %s.txt`, where the first `%s` is the penultimate version and the second `%s` is the current version.
10. Open the most recent `list diff %s %s.txt`.
11. Run *War Thunder*.
12. For every nation
    1. Open its *OpenDocument* spreadsheet.
    2. Update the table with the new entries from `list diff %s %s.txt`.
    3. Save the table as *CSV*, with a tab character as cell separator and nothing as text separator.
    4. Open the *CSV* as a text file and remove the last empty line.
13. Close *War Thunder*.
14. Copy the *CSV* files to the `Java` folder.
15. Run `WarThunderGroundAttackTestRangeGenerator` and choose the `Java` folder.
16. Copy the generated *BLK*s with names ending in `_screenshot` to *War Thunder*'s `UserMissions` folder.
17. Run *War Thunder* with *Movie* graphic settings and 1366x768 resolution.
18. For every mission
    1. Play the mission for a few seconds then exit it.
    2. Replay it.
    3. Take the screenshot with *Ansel* (*FOV* 90).
19. Zip everything and publish it.