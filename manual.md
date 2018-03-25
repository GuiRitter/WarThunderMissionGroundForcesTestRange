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
13. Run *War Thunder*.
14. For every nation
    1. Run `Java/WarThunderTreeTableManager`.
    2. Open `table/%s.csv` with it, where `%s` is the nation.
    3. Update the table with the new entries from `list diff %s %s.txt`.
    4. Overwrite `table/%s.csv`.
15. Close *War Thunder*.
16. Copy the *CSV* files to the `Java` folder.
17. Run `WarThunderGroundAttackTestRangeGenerator` and choose the `Java` folder.
18. Copy the generated *BLK*s with names ending in `_screenshot` to *War Thunder*'s `UserMissions` folder.
19. Run *War Thunder* with *Movie* graphic settings and 1366x768 resolution.
20. For every mission
    1. Play the mission for a few seconds then exit it.
    2. Replay it.
    3. Take the screenshot with *Ansel* (*FOV* 90).
21. Zip everything and publish it.