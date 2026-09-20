using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Threading;
using System.Windows.Automation;

namespace WarThunderModelLister
{
    /// <summary>
    /// Written entirely by Copilot after several rounds of prompting for code to inspect the UI.
    /// </summary>
    internal class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 1)
            {
                Console.WriteLine("Usage: <program> <output file path>");
                return;
            }

            string outputPath = args[0];

            // Find the process named 'daEditor3x-dev.exe'
            var process = Process.GetProcessesByName("daEditor3x-dev").FirstOrDefault();

            if (process == null)
            {
                Console.WriteLine("Process 'daEditor3x-dev.exe' not found.");
                return;
            }

            int pid = process.Id;
            Console.WriteLine($"Found process 'daEditor3x-dev.exe' with PID: {pid}");

            Stopwatch stopwatch = Stopwatch.StartNew(); // Start measuring time
            bool keepTracking = true;

            // Start a second thread to track elapsed time
            Thread timeTracker = new Thread(() =>
            {
                while (keepTracking)
                {
                    Console.WriteLine($"Elapsed time: {stopwatch.Elapsed.TotalSeconds:F1} seconds");
                    Thread.Sleep(10000); // Wait for 10 seconds
                }
            });

            timeTracker.Start();

            try
            {
                using (StreamWriter writer = new StreamWriter(outputPath))
                {
                    // Find the main window of the process
                    AutomationElement mainWindow = AutomationElement.RootElement.FindFirst(
                        TreeScope.Children,
                        new PropertyCondition(AutomationElement.ProcessIdProperty, pid)
                    );

                    if (mainWindow == null)
                    {
                        Console.WriteLine("No main window found for the specified PID.");
                        return;
                    }

                    Console.WriteLine("Main window found. Searching for the 'Class' combo box:");

                    // Find the 'Class' combo box
                    var classComboBox = mainWindow.FindFirst(
                        TreeScope.Descendants,
                        new AndCondition(
                            new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ComboBox),
                            new PropertyCondition(AutomationElement.NameProperty, "Class")
                        )
                    );

                    if (classComboBox == null)
                    {
                        Console.WriteLine("The 'Class' combo box was not found.");
                        return;
                    }

                    Console.WriteLine("'Class' combo box found. Logging details:");
                    Console.WriteLine($"  Name: {classComboBox.Current.Name}");
                    Console.WriteLine($"  IsEnabled: {classComboBox.Current.IsEnabled}");
                    Console.WriteLine($"  BoundingRectangle: {classComboBox.Current.BoundingRectangle}");

                    // Expand the combo box to load virtualized items
                    try
                    {
                        if (classComboBox.TryGetCurrentPattern(ExpandCollapsePattern.Pattern, out object expandPattern))
                        {
                            var expandCollapsePattern = (ExpandCollapsePattern)expandPattern;
                            expandCollapsePattern.Expand();
                            Console.WriteLine("  Expanded the combo box using ExpandCollapsePattern.");
                        }
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"  Failed to expand the combo box: {ex.Message}");
                    }

                    // Retrieve the ControlType.List child
                    var listChild = classComboBox.FindFirst(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.List));

                    if (listChild == null)
                    {
                        Console.WriteLine("  No ControlType.List child found in the combo box.");
                    }
                    else
                    {
                        Console.WriteLine("  ControlType.List child found. Logging details:");
                        Console.WriteLine($"    Name: {listChild.Current.Name}");
                        Console.WriteLine($"    BoundingRectangle: {listChild.Current.BoundingRectangle}");

                        // Retrieve items from the ControlType.List child
                        var listItems = listChild.FindAll(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ListItem));

                        Console.WriteLine($"    Total items in list: {listItems.Count}");
                        for (int i = 0; i < listItems.Count; i++)
                        {
                            writer.WriteLine(listItems[i].Current.Name);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred: {ex.Message}");
            }
            finally
            {
                keepTracking = false; // Stop the time tracker thread
                timeTracker.Join(); // Wait for the time tracker thread to finish
                stopwatch.Stop(); // Stop measuring time
                Console.WriteLine($"Time elapsed: {stopwatch.Elapsed.TotalSeconds} seconds");
            }
        }
    }
}
