import SwiftUI
import shared

@main
struct KmpFintechStarterApp: App {
    init() {
        IosModuleKt.initKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
