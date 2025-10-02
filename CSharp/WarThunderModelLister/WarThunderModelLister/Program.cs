using System;
using System.Diagnostics;
using System.IO;
using System.Threading;
using System.Windows.Automation;

namespace WarThunderModelLister
{
    internal class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Usage: <program> <PID> <output file path>");
                return;
            }

            if (!int.TryParse(args[0], out int pid))
            {
                Console.WriteLine("Invalid PID. Please enter a valid number.");
                return;
            }

            string outputPath = args[1];

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
                        writer.WriteLine("No main window found for the specified PID.");
                        return;
                    }

                    writer.WriteLine("Main window found. Searching for the 'Class' combo box:");

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
                        writer.WriteLine("The 'Class' combo box was not found.");
                        return;
                    }

                    writer.WriteLine("'Class' combo box found. Logging details:");
                    writer.WriteLine($"  Name: {classComboBox.Current.Name}");
                    writer.WriteLine($"  IsEnabled: {classComboBox.Current.IsEnabled}");
                    writer.WriteLine($"  BoundingRectangle: {classComboBox.Current.BoundingRectangle}");

                    // Expand the combo box to load virtualized items
                    try
                    {
                        if (classComboBox.TryGetCurrentPattern(ExpandCollapsePattern.Pattern, out object expandPattern))
                        {
                            var expandCollapsePattern = (ExpandCollapsePattern)expandPattern;
                            expandCollapsePattern.Expand();
                            writer.WriteLine("  Expanded the combo box using ExpandCollapsePattern.");
                        }
                    }
                    catch (Exception ex)
                    {
                        writer.WriteLine($"  Failed to expand the combo box: {ex.Message}");
                    }

                    // Retrieve the ControlType.List child
                    var listChild = classComboBox.FindFirst(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.List));

                    if (listChild == null)
                    {
                        writer.WriteLine("  No ControlType.List child found in the combo box.");
                    }
                    else
                    {
                        writer.WriteLine("  ControlType.List child found. Logging details:");
                        writer.WriteLine($"    Name: {listChild.Current.Name}");
                        writer.WriteLine($"    BoundingRectangle: {listChild.Current.BoundingRectangle}");

                        // Retrieve items from the ControlType.List child
                        var listItems = listChild.FindAll(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ListItem));

                        writer.WriteLine($"    Total items in list: {listItems.Count}");
                        for (int i = 0; i < listItems.Count; i++)
                        {
                            writer.WriteLine($"==================================================");
                            writer.WriteLine($"");
                            writer.WriteLine($"String            : {listItems[i].Current.Name}");
                            writer.WriteLine($"");
                            writer.WriteLine($"Value             : 0");
                            writer.WriteLine($"");
                            writer.WriteLine($"==================================================");
                            writer.WriteLine($"");
                            writer.WriteLine($"");
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
