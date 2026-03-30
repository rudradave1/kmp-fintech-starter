import SwiftUI

struct ContentView: View {
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 16) {
                Text("KMP Fintech Starter")
                    .font(.largeTitle.weight(.semibold))
                Text("SwiftUI can consume the shared Kotlin domain, repositories, and state holders exposed by the shared module.")
                    .font(.body)
                    .foregroundStyle(.secondary)
                List {
                    Label("Offline-first SQLDelight cache", systemImage: "internaldrive")
                    Label("Ktor networking with retry", systemImage: "network")
                    Label("Koin dependency graph", systemImage: "square.stack.3d.up")
                }
                .frame(maxHeight: 220)
            }
            .padding(24)
            .navigationTitle("Starter")
        }
    }
}
